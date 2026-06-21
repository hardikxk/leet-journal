package com.hardik.problemsservice.service;

import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalExecutorService {

    public String executeJava(String userCode){ // This will accept user's code input
        Path tempFile = null;
        try{
            tempFile = Files.createTempFile("run-", ".java");
            // User's code is written directly to the temporary file
            Files.writeString(tempFile, userCode);

            ProcessBuilder pb = new ProcessBuilder("java",  tempFile.toString());
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String output = new String(process.getInputStream().readAllBytes());

            // Set a timeout of 5 seconds to handle potential infinite loops
            boolean finished = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);

            if (!finished) {
            process.destroyForcibly(); // Force stop
            return "Execution Failed: Time Limit Exceeded (Timeout)";
        }

            return output;
        }
        catch(Exception e){
            return "Execution Failed : \n" + e.getMessage();
        }
        finally {
            if (tempFile != null) {
                IO.println(tempFile.toFile().delete());
            }
        }

    }
}
