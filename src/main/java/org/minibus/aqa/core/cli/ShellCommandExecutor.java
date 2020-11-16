package org.minibus.aqa.core.cli;


import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShellCommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellCommandExecutor.class);
    private static final int COMMAND_TIMEOUT = 15;
    public static final long DEFAULT_PID = -1;

    public static ShellCommandResult exec(final String... cmdParts) {
        LOGGER.debug(String.join(" ", cmdParts));

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

            return new ShellCommandResult(pid, exitCode, startTime, resolveOutput(process.getInputStream()), resolveOutput(process.getErrorStream()));
        } catch (IOException | InterruptedException e) {
            return new ShellCommandResult(pid,1, startTime, StringUtils.EMPTY, e.getMessage());
        }
    }

    public static boolean killAll(final String processName) {
        LOGGER.debug(String.format("Trying to kill all processes by the given name: '%s'", processName));

        AtomicBoolean res = new AtomicBoolean(false);

        ProcessHandle.allProcesses()
                .filter(ProcessHandle::isAlive)
                .forEach(ph -> ph.info().command().filter(cmd -> cmd.contains(processName)).ifPresentOrElse(cmd -> {
                    LOGGER.debug(String.format("The process has found by '%s' name, pid=%d, killing...", processName, ph.pid()));
                    res.set(ph.destroy());
                }, () -> {
                    LOGGER.debug(String.format("No processes were found by the given name: '%s'", processName));
                }));

        return res.getOpaque();
    }

    public static boolean kill(final long pid) {
        LOGGER.debug(String.format("Trying to kill the process with pid=%d", pid));

        AtomicBoolean result = new AtomicBoolean(false);

        ProcessHandle.allProcesses()
                .filter(ProcessHandle::isAlive)
                .filter(ph -> ph.pid() == pid)
                .findFirst()
                .ifPresentOrElse(ph -> {
                    LOGGER.debug(String.format("The process has found by the given pid=%d, killing...", pid));
                    result.set(ph.destroy());
                }, () -> {
                    LOGGER.debug(String.format("The process wasn't found by the given pid=%d, nothing to kill", pid));
                });

        return result.getOpaque();
    }

    public static boolean isPortOpened(final int port) {
        LOGGER.debug(String.format("Checking whether %d port is opened", port));

        String out = exec("lsof", "-i", ":" + port).getStdout();

        if (out.isEmpty()) {
            LOGGER.debug(String.format("Port %d is opened", port));
            return true;
        } else {
            LOGGER.debug(String.format("Port %d is in use", port));
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
            LOGGER.error(String.format("Can't resolve output from process:\n%s", e.getMessage()));
            return StringUtils.EMPTY;
        }

        return output.toString();
    }
}
