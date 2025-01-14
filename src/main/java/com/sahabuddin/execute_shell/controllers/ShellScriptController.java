package com.sahabuddin.execute_shell.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class ShellScriptController {

    @GetMapping("/execute-shell")
    public String executeShellScript() {
        String scriptPath = "/home/sahabuddin/script.sh"; // Replace with your script's path
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
            processBuilder.redirectErrorStream(true);

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
