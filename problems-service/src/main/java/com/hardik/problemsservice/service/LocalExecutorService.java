package com.hardik.problemsservice.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class LocalExecutorService {

    private static final Logger log = LoggerFactory.getLogger(LocalExecutorService.class);

    public String executeJava(){
        Path tempDirectory = null;
        Path tempSourceFile = null;
        try{
            Resource classPathResource = new ClassPathResource("BinarySearch.java");
            String sourceFileName = classPathResource.getFilename();
            if (sourceFileName == null || sourceFileName.isBlank()) {
                throw new IllegalStateException("Source file name is missing");
            }

            tempDirectory = Files.createTempDirectory("java-run-");
            tempSourceFile = tempDirectory.resolve(sourceFileName);

            try (InputStream inputStream = classPathResource.getInputStream()) {
                Files.copy(inputStream, tempSourceFile);
            }

            String compileOutput = runProcess(tempDirectory, "javac", tempSourceFile.toString());
            if (!compileOutput.isBlank()) {
                log.debug("javac output: {}", compileOutput);
            }

            String className = sourceFileName.substring(0, sourceFileName.lastIndexOf('.'));
            return runProcess(tempDirectory, "java", "-cp", tempDirectory.toString(), className);
        }
        catch(Exception e){
            log.error("Failed to execute Java source", e);
            return "Execution Failed : \n" + e.getMessage();
        }
        finally {
            cleanup(tempSourceFile, tempDirectory);
        }

    }

    private String runProcess(Path workingDirectory, String... command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workingDirectory.toFile());
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IllegalStateException(output.isBlank() ? "Command failed with exit code " + exitCode : output.trim());
        }

        return output;
    }

    private void cleanup(Path tempSourceFile, Path tempDirectory) {
        try {
            if (tempDirectory != null && Files.exists(tempDirectory)) {
                Files.walk(tempDirectory)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException cleanupException) {
                                log.warn("Failed to delete temporary path {}", path, cleanupException);
                            }
                        });
            } else if (tempSourceFile != null) {
                Files.deleteIfExists(tempSourceFile);
            }
        } catch (IOException cleanupException) {
            log.warn("Failed to clean temporary Java execution files", cleanupException);
        }
    }
}
