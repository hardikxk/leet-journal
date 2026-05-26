package com.hardik.problemsservice.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

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

            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return "Execution Failed : \nExecution timed out after 30 seconds.";
            }

            String output = new String(process.getInputStream().readAllBytes());

            return output;
        }
        catch(Exception e){
            return "Execution Failed : \n" + e.getMessage();
        }
        finally {
            if (tempFile != null) {
                System.out.println(tempFile.toFile().delete());
            }
        }

    }
}
