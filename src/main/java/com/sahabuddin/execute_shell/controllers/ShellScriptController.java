package com.sahabuddin.execute_shell.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class ShellScriptController {

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.hostname}")
    private String hostname;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remote-dir}")
    private String remoteDir;

    @Value("${sftp.local-dir}")
    private String localDir;

    @GetMapping("/execute-shell")
    public String executeShellScriptWithEnv() {
        String scriptPath = "/home/sahabuddin/script.sh"; // Replace with your script's path
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
            processBuilder.redirectErrorStream(true);

            // Set environment variables
            processBuilder.environment().put("SFTP_USER", username);
            processBuilder.environment().put("SFTP_HOST", hostname);
            processBuilder.environment().put("SFTP_PORT", String.valueOf(port));
            processBuilder.environment().put("SFTP_PASSWORD", password);
            processBuilder.environment().put("SFTP_REMOTE_DIR", remoteDir);
            processBuilder.environment().put("LOCAL_DIR", localDir);

            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "Script executed successfully:\n" + output;
            } else {
                return "Script execution failed with exit code: " + exitCode + "\nOutput:\n" + output;
            }
        } catch (Exception e) {
            return "Error executing script: " + e.getMessage();
        }
    }



}
