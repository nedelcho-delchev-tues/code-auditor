package com.code.auditor.listeners;

import com.code.auditor.configuration.RabbitMQConfig;
import com.code.auditor.domain.StudentSubmission;
import com.code.auditor.repositories.StudentSubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class SubmissionSavedListener {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionSavedListener.class);

    private final StudentSubmissionRepository studentSubmissionRepository;

    public SubmissionSavedListener(StudentSubmissionRepository studentSubmissionRepository) {
        this.studentSubmissionRepository = studentSubmissionRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleRecordInsertedEvent(Long submissionID) {
        StudentSubmission ss = studentSubmissionRepository.findById(submissionID).orElseThrow();
        if (!checkIfSpecialFilesPresent(ss)){
            logger.info("files are not present");
        }
        logger.info("all files are present");
    }

    private boolean checkIfSpecialFilesPresent(StudentSubmission ss) {
        List<String> specialFiles = ss.getAssignment().getSpecialFiles();
        if ( specialFiles == null || specialFiles.isEmpty()) {
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
            e.printStackTrace();
        }

        return foundFiles.containsAll(specialFiles);
    }

    private String getFileNameFromEntry(ZipEntry entry) {
        String fullPath = entry.getName();
        return fullPath.substring(fullPath.lastIndexOf('/') + 1);
    }
}
