package org.minibus.aqa.core.cli;

import org.minibus.aqa.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.minibus.aqa.core.cli.ShellCommandExecutor.DEFAULT_PID;

public class ShellCommandResult {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellCommandResult.class);
    private final String pid;
    private final int exitCode;
    private final int executionTime;
    private final String stdout;
    private final String rawStdout;
    private final String stderr;

    public ShellCommandResult(long pid, int exitCode, long startTime, String stdout, String stderr) {
        this.executionTime = (int) (System.nanoTime() - startTime) / 1000000;
        this.pid = String.valueOf(pid == DEFAULT_PID ? Constants.NULL : pid);
        this.exitCode = exitCode;
        this.rawStdout = stdout == null ? "" : stdout.trim();
        this.stderr = stderr == null ? "" : stderr.trim();
        this.stdout = this.rawStdout.replaceAll("[\\t\\n\\r\\s]+"," ").trim();

        LOGGER.debug(String.format("time: %dms, pid: %d, code: %d", executionTime, pid, exitCode));

        if (exitCode == 0) {
            if (!this.stdout.isEmpty()) {
                LOGGER.debug(String.format("stdout: %s", this.stdout));
            }
        } else {
            if (!this.stderr.isEmpty()) {
                LOGGER.error(String.format("stderr: %s", this.stderr));
            }
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
