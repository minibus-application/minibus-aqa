package org.minibus.aqa.core.common.cli;

import org.apache.commons.lang3.ArrayUtils;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.helpers.RandomGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdbCommandExecutor extends ShellCommandExecutor implements AdbCommand {

    private static final String DEFAULT_ADB_DEVICES_PATTERN = "([a-zA-Z0-9\\\\-]+)\\t(%s)";
    private static final String DEFAULT_ADB_INFO_PATTERN = "%s:(\\S+|$)";

    public static File takeScreenshot() {
        return takeScreenshot(null);
    }

    public static File takeScreenshot(String udid) {
        String filePath = Constants.DEVICE_SDCARD + RandomGenerator.temp();
        adb(udid, SHELL, SCREENCAP, filePath).waitFor();

        return new File(filePath);
    }

    public static File pull(String fileOrigin) {
        return pull(null, fileOrigin);
    }

    public static File pull(String udid, String fileOrigin) {
        String fileDestination = Constants.PROJECT_TEMP_FOLDER;
        adb(udid, PULL, fileOrigin, fileDestination).waitFor();

        return new File(fileDestination);
    }

    public static int getDevicesCount() {
        String out = exec(ADB, DEVICES).getOutput();

        Pattern pattern = Pattern.compile(getAdbDevicesPattern());
        Matcher matcher = pattern.matcher(out);
        return (int) matcher.results().count();
    }

    public static int getDevicesCount(DeviceState state) {
        String out = exec(ADB, DEVICES).getOutput();

        Pattern pattern = Pattern.compile(getAdbDevicesPattern(state));
        Matcher matcher = pattern.matcher(out);
        return (int) matcher.results().count();
    }

    public static List<String> getDevices() {
        List<String> matches = new ArrayList<>();
        String out = exec(ADB, DEVICES).getOutput();

        Pattern pattern = Pattern.compile(getAdbDevicesPattern());
        Matcher matcher = pattern.matcher(out);

        while (matcher.find()) {
            matches.add(matcher.group(1));
        }

        return matches;
    }

    public static String getDevices(String delimiter) {
        return String.join(delimiter, getDevices());
    }

    public static List<String> getDevices(DeviceState state) {
        List<String> matches = new ArrayList<>();
        String out = exec(ADB, DEVICES).getOutput();

        Pattern pattern = Pattern.compile(getAdbDevicesPattern(state));
        Matcher matcher = pattern.matcher(out);

        while (matcher.find()) {
            matches.add(matcher.group(1));
        }

        return matches;
    }

    public static String getDevices(DeviceState state, String delimiter) {
        return String.join(delimiter, getDevices(state));
    }

    public static DeviceState getDeviceState(String udid) {
        String state = adb(udid, STATE).getOutput();

        try {
            return DeviceState.valueOf(state);
        } catch (IllegalArgumentException e) {
            return DeviceState.UNDEFINED;
        }
    }

    public static String getDeviceInfo(DeviceInfo info) {
        String out = exec(ADB, DEVICES, "-l").getOutput();

        Pattern pattern = Pattern.compile(String.format(DEFAULT_ADB_INFO_PATTERN, info.get()));
        Matcher matcher = pattern.matcher(out);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "null";
        }
    }

    public static boolean startAdbd() {
        LocalProcess p = exec(ADB, START_ADBD);
        int ec = p.waitFor();
        String out = p.getOutput();

        return ec != 1 && (out.isEmpty() || out.contains("successfully"));
    }

    public static boolean killAdbd() {
        LocalProcess p = exec(ADB, KILL_ADBD);
        int ec = p.waitFor();

        return ec != 1 && !isProcessExist(ADB);
    }

    private static LocalProcess adb(String udid, String... cmdParts) {
        int devicesCount = getDevicesCount();

        if ((udid == null || udid.isEmpty()) && devicesCount == 1) {
            return exec(ArrayUtils.addAll(new String[] {ADB}, cmdParts));
        } else if ((udid == null || udid.isEmpty()) && devicesCount > 1) {
            String e = String.format("UDID is null, but number of connected devices is grater than 1: %s", getDevices(", "));
            throw new UnsupportedOperationException(e);
        } else if (devicesCount == 0) {
            throw new UnsupportedOperationException("Can't execute ADB, number of connected devices is 0");
        } else {
            return exec(ArrayUtils.addAll(new String[] {ADB, "-s", udid}, cmdParts));
        }
    }

    private static String getAdbDevicesPattern() {
        return String.format(DEFAULT_ADB_DEVICES_PATTERN,
                Arrays.stream(DeviceState.values()).map(DeviceState::get).collect(Collectors.joining("|")));
    }

    private static String getAdbDevicesPattern(DeviceState state) {
        return String.format(DEFAULT_ADB_DEVICES_PATTERN, state.get());
    }
}
