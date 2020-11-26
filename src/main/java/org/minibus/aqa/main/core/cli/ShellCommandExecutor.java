package org.minibus.aqa.main.core.cli;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShellCommandExecutor {
    private static final Logger LOGGER = LogManager.getLogger(ShellCommandExecutor.class);
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
        AtomicBoolean res = new AtomicBoolean(false);
        ProcessHandle.allProcesses()
                .filter(ProcessHandle::isAlive)
                .forEach(ph -> ph.info().command().filter(cmd -> cmd.contains(processName)).ifPresent(cmd -> {
                    LOGGER.debug("Kill '{}' process, pid={}", processName, ph.pid());
                    res.set(ph.destroy());
                }));

        return res.getOpaque();
    }

    public static boolean kill(final long pid) {
        AtomicBoolean result = new AtomicBoolean(false);
        ProcessHandle.allProcesses()
                .filter(ProcessHandle::isAlive)
                .filter(ph -> ph.pid() == pid)
                .findFirst()
                .ifPresent(ph -> {
                    LOGGER.debug("Kill the process, pid={}", pid);
                    result.set(ph.destroy());
                });

        return result.getOpaque();
    }

    public static boolean isPortOpened(final int port) {
        String out = exec("lsof", "-i", ":" + port).getStdout();

        if (out.isEmpty()) {
            LOGGER.debug("Port {} is opened", port);
            return true;
        } else {
            LOGGER.debug("Port {} is in use", port);
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
            LOGGER.fatal("Can not resolve output from the process", e);
            return StringUtils.EMPTY;
        }

        return output.toString();
    }
}
