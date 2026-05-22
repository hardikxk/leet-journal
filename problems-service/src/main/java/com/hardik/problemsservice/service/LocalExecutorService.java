package com.hardik.problemsservice.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class LocalExecutorService {

    public String executeJava(){
        Path tempFile = null;
        try{
            Resource classPathResource = new ClassPathResource("BinarySearch.java");
            tempFile = Files.createTempFile("run-", ".java");
            Files.copy(classPathResource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            ProcessBuilder pb = new ProcessBuilder("java",  tempFile.toString());
            pb.redirectErrorStream(true);
            Process process = pb.start();

            java.util.concurrent.CompletableFuture<String> outputFuture = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                try {
                    return new String(process.getInputStream().readAllBytes());
                } catch (Exception e) {
                    return "";
                }
            });

            boolean finished = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return "Execution Failed : Timeout exceeded";
            }

            return outputFuture.get();
        }
        catch(Exception e){
            return "Execution Failed : \n" + e.getMessage();
        }
        finally {
            if (tempFile != null) {
                System.out.println("Deleted temp file: " + tempFile.toFile().delete());
            }
        }

    }
}
