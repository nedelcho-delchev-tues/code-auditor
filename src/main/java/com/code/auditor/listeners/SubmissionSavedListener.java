package com.code.auditor.listeners;

import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.repositories.StudentSubmissionRepository;
import com.code.auditor.services.DatabaseCreationService;
import com.code.auditor.utils.HTMLTemplates;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.code.auditor.utils.HTMLTemplates.convertToHtml;

@Component
public class SubmissionSavedListener {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionSavedListener.class);
    private static final String mavenHome = System.getenv("MAVEN_HOME");
    private static final String tempDir = System.getProperty("java.io.tmpdir");
    private String databaseName;
    private String mavenOutput;
    private final StudentSubmissionRepository studentSubmissionRepository;
    private final DatabaseCreationService databaseCreationService;

    public SubmissionSavedListener(StudentSubmissionRepository studentSubmissionRepository, DatabaseCreationService databaseCreationService) {
        this.studentSubmissionRepository = studentSubmissionRepository;
        this.databaseCreationService = databaseCreationService;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getMavenOutput() {
        return mavenOutput;
    }

    public void setMavenOutput(String mavenOutput) {
        this.mavenOutput = mavenOutput;
    }

    @RabbitListener(queues = "student_submission_queue")
    public void handleRecordInsertedEvent(Long submissionID) {
        try {
            StudentSubmission ss = studentSubmissionRepository.findById(submissionID).orElseThrow();
            if (!checkIfSpecialFilesPresent(ss)) {
                ss.setFilesPresent(false);
                logger.error("files are not present");
            } else {
                ss.setFilesPresent(true);
            }
            logger.info("all files are present");

            //Project name might be different from zip name
            String projectName = unzipProjectAndGetParentDirName(ss.getContent());

            String mavenProjectDir = tempDir + FilenameUtils.removeExtension(projectName);
            String mavenProjectPom = mavenProjectDir + File.separator + "pom.xml";
            String mavenProjectSpotBugs = mavenProjectDir + "target" + File.separator + "spotbugs.html";

            try {
                databaseCreationService.createDatabaseIfNotExists(getDatabaseName());
            } catch (Exception e) {
                logger.error("Build has failed because the project's database could not be created");
                ss.setProblems(HTMLTemplates.DB_NOT_CREATED.getBytes());
            }

            addSpotBugsPlugin(mavenProjectPom);

            InvocationResult result = executeExtractedProject(mavenProjectDir);

            if (result.getExitCode() != 0) {
                ss.setBuildPassing(false);
                logger.error("Build has failed !" + getMavenOutput());
                ss.setProblems(convertToHtml(getMavenOutput()).getBytes());
            } else {
                ss.setBuildPassing(true);
            }

            byte[] spotBugsReport = getSpotBugsReport(mavenProjectSpotBugs);

            if (spotBugsReport.length > 0) {
                ss.setProblems(spotBugsReport);
            } else {
                ss.setProblems(HTMLTemplates.SPOTBUGS_NOT_FOUND.getBytes());
            }

            studentSubmissionRepository.save(ss);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            e.printStackTrace();
        }
    }

    private byte[] getSpotBugsReport(String mavenProjectSpotBugs) throws IOException {
        Path path = Path.of(mavenProjectSpotBugs);

        try {
            Files.exists(path);
            return editSpotBugsReport(Files.readAllBytes(path));
        } catch (IOException e) {
            logger.error("File not found: " + mavenProjectSpotBugs);
        }
        return new byte[0];
    }

    private byte[] editSpotBugsReport(byte[] htmlContent) {
        String htmlContentString = new String(htmlContent, StandardCharsets.UTF_8);

        Document document = Jsoup.parse(htmlContentString);

        // Remove the "Code analyzed:" part
        Element codeAnalyzed = document.selectFirst("p:containsOwn(Code analyzed:)");
        if (codeAnalyzed != null) {
            codeAnalyzed.remove();
        }

        // Remove the <ul> that follows
        Element ulFollowingCodeAnalyzed = document.selectFirst("p:containsOwn(Code analyzed:) + ul");
        if (ulFollowingCodeAnalyzed != null) {
            ulFollowingCodeAnalyzed.remove();
        }

        Element liProjectPath = document.selectFirst("li:containsOwn(" + tempDir + ")");
        if (liProjectPath != null) {
            liProjectPath.remove();
        }

        String editedHtml = document.html();

        return editedHtml.getBytes(StandardCharsets.UTF_8);
    }

    private boolean checkIfSpecialFilesPresent(StudentSubmission ss) {
        List<String> specialFiles = ss.getAssignment().getSpecialFiles();
        if (specialFiles == null || specialFiles.isEmpty()) {
            return true;
        }

        Set<String> foundFiles = new HashSet<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(ss.getContent()))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String filePath = tempDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    String fileName = getFileNameFromEntry(entry);
                    if (fileName.equals("application.properties") || fileName.equals("application.yml")) {
                        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
                        setDatabaseName(extractDatabaseNameFromContent(fileContent, FileNameUtils.getExtension(fileName), filePath));
                    }
                    foundFiles.add(fileName);
                }
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            logger.error(String.valueOf(e));
            e.printStackTrace();
        }

        return foundFiles.containsAll(specialFiles);
    }

    private String unzipProjectAndGetParentDirName(byte[] submissionContent) {
        String parentDirName = null;
        byte[] buffer = new byte[1024];
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(submissionContent));

        try (zipInputStream) {
            ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
            while (entry != null) {
                String filePath = tempDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                } else if (parentDirName == null) {
                    parentDirName = entry.getName();
                }
                entry = zipInputStream.getNextZipEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parentDirName;
    }

    private String getFileNameFromEntry(ZipEntry entry) {
        String fullPath = entry.getName();
        return fullPath.substring(fullPath.lastIndexOf('/') + 1);
    }

    private String extractDatabaseNameFromContent(String content, String fileType, String filePath) {
        if ("properties".equalsIgnoreCase(fileType)) {
            return extractFromProperties(content);
        } else if ("yml".equalsIgnoreCase(fileType)) {
            return extractAndModifyYml(content, filePath);
        }
        return null;
    }

    private String extractFromProperties(String content) {
        String[] lines = content.split("\n");

        String dbUrl = Arrays.stream(lines)
                .filter(line -> line.trim().startsWith("spring.datasource.url="))
                .findFirst()
                .orElse(null);

        if (dbUrl == null) {
            return null;
        }

        String url = dbUrl.split("=", 2)[1].trim();

        return extractDatabaseNameFromUrl(url);
    }

    @SuppressWarnings("unchecked")
    private String extractAndModifyYml(String content, String filePath) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = yaml.load(content);

        Map<String, Object> springMap = (Map<String, Object>) yamlMap.get("spring");
        if (springMap == null) {
            return null;
        }

        Map<String, Object> datasourceMap = (Map<String, Object>) springMap.get("datasource");
        if (datasourceMap == null) {
            return null;
        }

        String url = (String) datasourceMap.get("url");

        datasourceMap.put("username", "postgres");
        datasourceMap.put("password", "postgres");

        String updatedContent = yaml.dump(yamlMap);

        try {
            Files.writeString(Paths.get(filePath), updatedContent);
        } catch (IOException e) {
            logger.error("Failed to extract or modify yaml" + e.getMessage());
        }

        if (url == null) {
            return null;
        }

        return extractDatabaseNameFromUrl(url);
    }

    private String extractDatabaseNameFromUrl(String url) {
        int lastIndex = url.lastIndexOf("/");
        if (lastIndex != -1) {
            int paramIndex = url.indexOf("?", lastIndex);
            if (paramIndex != -1) {
                return url.substring(lastIndex + 1, paramIndex);
            } else {
                return url.substring(lastIndex + 1);
            }
        }
        return null;
    }

    private InvocationResult executeExtractedProject(String workingDir) throws Exception {
        try {
            Path mvn = Paths.get(mavenHome);

            InvocationRequest request = new DefaultInvocationRequest();

            request.setInputStream(InputStream.nullInputStream());
            request.setGoals(Arrays.asList("clean", "install"));
            request.setBaseDirectory(new File(workingDir));
            request.setMavenHome(mvn.toFile());

            DefaultInvoker invoker = new DefaultInvoker();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            invoker.setOutputHandler(new PrintStreamHandler(new PrintStream(out), true));

            InvocationResult result = invoker.execute(request);

            setMavenOutput(out.toString());

            return result;
        } catch (Exception e) {
            System.err.println(": " + e.getMessage());
            throw new Exception("Error executing Maven command!" + e.getMessage());
        }
    }

    private void addSpotBugsPlugin(String pomFilePath) {
        try {
            String configuration = "<configuration> " +
                    "<effort>default</effort> " +
                    "<reportLevel>high</reportLevel> " +
                    "<htmlOutput>true</htmlOutput> " +
                    "<failOnError>false</failOnError>" +
                    "</configuration>";
            Model model = parsePomXmlFileToMavenPomModel(pomFilePath);

            Xpp3Dom pluginConfiguration = Xpp3DomBuilder.build(new StringReader(configuration));

            List<PluginExecution> pluginExecutions = new ArrayList<>();
            PluginExecution pluginExecution = new PluginExecution();
            pluginExecution.addGoal("check");
            pluginExecution.setPhase("install");
            pluginExecutions.add(pluginExecution);

            Plugin spotBugs = new Plugin();
            spotBugs.setGroupId("com.github.spotbugs");
            spotBugs.setArtifactId("spotbugs-maven-plugin");
            spotBugs.setVersion("4.7.3.5");
            spotBugs.setConfiguration(pluginConfiguration);
            spotBugs.setExecutions(pluginExecutions);

            model.getBuild().addPlugin(spotBugs);

            parseMavenPomModelToXmlString(pomFilePath, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Model parsePomXmlFileToMavenPomModel(String path) throws Exception {
        Model model;
        FileReader reader;

        MavenXpp3Reader mavenReader = new MavenXpp3Reader();
        reader = new FileReader(path);
        model = mavenReader.read(reader);

        return model;
    }

    private void parseMavenPomModelToXmlString(String path, Model model) throws Exception {
        MavenXpp3Writer mavenWriter = new MavenXpp3Writer();
        Writer writer = new FileWriter(path);
        mavenWriter.write(writer, model);
    }
}
