package com.code.auditor.listeners;

import com.code.auditor.configuration.RabbitMQConfig;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.repositories.StudentSubmissionRepository;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.digester.annotations.rules.SetProperty;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class SubmissionSavedListener {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionSavedListener.class);
    private final StudentSubmissionRepository studentSubmissionRepository;
    private static final String mavenHome = System.getenv("MAVEN_HOME");

    public SubmissionSavedListener(StudentSubmissionRepository studentSubmissionRepository) {
        this.studentSubmissionRepository = studentSubmissionRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleRecordInsertedEvent(Long submissionID) {
        try {
            StudentSubmission ss = studentSubmissionRepository.findById(submissionID).orElseThrow();
            if (!checkIfSpecialFilesPresent(ss)) {
                // save into future AssignmentProblems table
                logger.error("files are not present");
            }

            logger.info("all files are present");

            String tempDir = System.getProperty("java.io.tmpdir");
            unzipProject(ss.getContent(), tempDir);

            String mavenProjectDir = tempDir + FilenameUtils.removeExtension(ss.getFileName());

            executeExtractedProject(mavenProjectDir);

        } catch (Exception e) {
            logger.error(String.valueOf(e));
            e.printStackTrace();
        }
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
                if (!entry.isDirectory()) {
                    String fileName = getFileNameFromEntry(entry);
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

    private String getFileNameFromEntry(ZipEntry entry) {
        String fullPath = entry.getName();
        return fullPath.substring(fullPath.lastIndexOf('/') + 1);
    }

    private void unzipProject(byte[] submissionContent, String destinationDir) {
        byte[] buffer = new byte[1024];
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(submissionContent),
                CharsetNames.UTF_8,
                true,
                true);

        try (zipInputStream) {
            ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
            while (entry != null) {
                String filePath = destinationDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    new File(filePath).getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                entry = zipInputStream.getNextZipEntry();
            }
        } catch (IOException e) {
            logger.error(String.valueOf(e));
            e.printStackTrace();
        }

    }

    private void executeExtractedProject(String workingDir) throws Exception {
        try {
            Path mvn = Paths.get(mavenHome);

            InvocationRequest request = new DefaultInvocationRequest();

            request.setInputStream(InputStream.nullInputStream());
            request.setGoals(Arrays.asList("clean", "install"));
            request.setBaseDirectory(new File(workingDir));
            request.setMavenHome(mvn.toFile());

            DefaultInvoker invoker = new DefaultInvoker();

            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                // save into future AssignmentProblems table
                logger.error("Build has failed !" + result.getExecutionException());

            }
        } catch (Exception e) {
            System.err.println("Error executing Maven command: " + e.getMessage());
            throw new Exception("Build has failed !" + e.getMessage());
        }
    }
}
