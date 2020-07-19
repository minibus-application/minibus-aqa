package org.minibus.aqa.core.common.cli;


import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.common.handlers.TestLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShellCommandExecutor {

    private static final int COMMAND_TIMEOUT = 15;
    private static final long DEFAULT_PID = -1;

    public static ProcessResult exec(final String... cmdParts) {
        TestLogger.get().debug(String.join(" ", cmdParts));

        ProcessBuilder processBuilder = new ProcessBuilder();
        long startTime = System.nanoTime();
        long pid = DEFAULT_PID;

        try {
            Process process = processBuilder.command(cmdParts).start();
            pid = process.pid();

            if (!process.waitFor(COMMAND_TIMEOUT, TimeUnit.SECONDS)) {
                throw new InterruptedException(String.format("'%s' command execution time has elapsed, timeout %d sec",
                        cmdParts[0], COMMAND_TIMEOUT));
            }

            int exitCode = process.exitValue();

            if (exitCode != 0) {
                TestLogger.get().error(String.format("'%s' command execution has failed", cmdParts[0]));
            }

            return new ProcessResult(pid, exitCode, startTime, resolveOutput(process.getInputStream()), resolveOutput(process.getErrorStream()));
        } catch (IOException | InterruptedException e) {
            return new ProcessResult(pid,1, startTime, StringUtils.EMPTY, e.getMessage());
        }
    }

    public static boolean killAll(final String processName) {
        TestLogger.get().debug(String.format("Trying to kill all processes by the given name: '%s'", processName));

        AtomicBoolean res = new AtomicBoolean(false);

        ProcessHandle.allProcesses()
                .filter(ProcessHandle::isAlive)
                .forEach(ph -> ph.info().command().filter(cmd -> cmd.contains(processName)).ifPresentOrElse(cmd -> {
                    TestLogger.get().debug(String.format("The process has found by '%s' name, pid=%d, killing...", processName, ph.pid()));
                    res.set(ph.destroy());
                }, () -> {
                    TestLogger.get().debug(String.format("No processes were found by the given name: '%s'", processName));
                }));

        return res.getOpaque();
    }

    public static boolean kill(final long pid) {
        TestLogger.get().debug(String.format("Trying to kill the process with pid=%d", pid));

        AtomicBoolean result = new AtomicBoolean(false);

        ProcessHandle.allProcesses()
                .filter(ProcessHandle::isAlive)
                .filter(ph -> ph.pid() == pid)
                .findFirst()
                .ifPresentOrElse(ph -> {
                    TestLogger.get().debug(String.format("The process has found by the given pid=%d, killing...", pid));
                    result.set(ph.destroy());
                }, () -> {
                    TestLogger.get().debug(String.format("The process wasn't found by the given pid=%d, nothing to kill", pid));
                });

        return result.getOpaque();
    }

    public static boolean isPortOpened(final int port) {
        TestLogger.get().debug(String.format("Checking whether %d port is opened", port));

        String out = exec("lsof", "-i", ":" + port).getStdout();

        if (out.isEmpty()) {
            TestLogger.get().debug(String.format("Port %d is opened", port));
            return true;
        } else {
            TestLogger.get().debug(String.format("Port %d is in use", port));
            return false;
        }
    }

    public static boolean isAlive(final String processName) {
        Optional<ProcessHandle> processHandleOptional = ProcessHandle.allProcesses()
                .filter(p -> StringUtils.containsIgnoreCase(p.info().command().get(), processName) && p.isAlive())
                .findFirst();

        return processHandleOptional.isPresent();
    }

    private static String resolveOutput(final InputStream stream) {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append(Constants.NEW_LINE);
            }
        } catch (IOException e) {
            TestLogger.get().error(String.format("Can't resolve output from process:\n%s", e.getMessage()));
            return "";
        }

        return output.toString();
    }

    public static class ProcessResult {

        private final String pid;
        private final int exitCode;
        private final int executionTime;
        private final String stdout;
        private final String rawStdout;
        private final String stderr;

        public ProcessResult(long pid, int exitCode, long startTime, String stdout, String stderr) {
            this.executionTime = (int) (System.nanoTime() - startTime) / 1000000;
            this.pid = String.valueOf(pid == DEFAULT_PID ? Constants.NULL : pid);
            this.exitCode = exitCode;
            this.rawStdout = stdout == null ? "" : stdout.trim();
            this.stderr = stderr == null ? "" : stderr.trim();
            this.stdout = this.rawStdout.replaceAll("[\\t\\n\\r\\s]+"," ").trim();

            TestLogger.get().debug(String.format("time: %dms, pid: %d, code: %d", executionTime, pid, exitCode));

            if (exitCode == 0) {
                if (!this.stdout.isEmpty()) {
                    TestLogger.get().debug(String.format("stdout: %s", this.stdout));
                }
            } else {
                TestLogger.get().error(String.format("stderr: %s", this.stderr));
            }
        }

        public String getRawStdout() {
            return rawStdout;
        }

        public String getStdout() {
            return stdout;
        }

        public String getStderr() {
            return stderr;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getPid() {
            return pid;
        }

        public boolean isSucceeded() {
            return exitCode == 0;
        }

        public int getDuration() {
            return executionTime;
        }
    }
}
