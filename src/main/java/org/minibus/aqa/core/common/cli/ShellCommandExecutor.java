package org.minibus.aqa.core.common.cli;


import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ShellCommandExecutor {

    public static LocalProcess exec(String cmd) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LocalProcess(process);
    }

    public static LocalProcess exec(String... cmdParts) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = null;

        try {
            processBuilder.command(cmdParts);
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LocalProcess(process);
    }

    public static class LocalProcess {

        private Process process;
        private int exitCode;

        public LocalProcess(Process process) {
            this.process = process;
        }

        public String getOutput() {
            String line;
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            try {
                while ((line = reader.readLine()) != null) output.append(line + Constants.NEW_LINE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return output.toString();
        }

        public int waitFor() {
            try {
                exitCode = process.waitFor();
            } catch(InterruptedException e) {
                exitCode = 1;
            }
            return exitCode;
        }

        public long getPid() {
            return process.pid();
        }

        public int getExitCode() {
            return exitCode;
        }

        public boolean isSucceeded() {
            return exitCode == 0;
        }
    }

    public static boolean isProcessExist(String process) {
        Optional<ProcessHandle> processHandleOptional = ProcessHandle.allProcesses()
                .filter(p -> StringUtils.containsIgnoreCase(p.info().command().get(), process) && p.isAlive())
                .findFirst();

        return processHandleOptional.isPresent();
    }

    public enum ShellType {
        CMD("cmd.exe"),
        SHELL("sh"),
        BASH("bash");

        private final String shellType;

        ShellType(String shellType) {
            this.shellType = shellType;
        }

        public String get() {
            return shellType;
        }
    }
}
