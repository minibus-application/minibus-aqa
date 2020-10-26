package org.minibus.aqa.core.common.cli;

import org.minibus.aqa.Constants;

import java.util.HashMap;
import java.util.Map;

public interface AdbCommand {
    String ADB = "adb";
    String SHELL = "shell";
    String GETPROP = "getprop";
    String SCREENCAP = "screencap";
    String DEVICES = "devices";
    String PULL = "pull";
    String STATE = "get-state";
    String START_ADBD = "start-server";
    String KILL_ADBD = "kill-server";

    enum DeviceInfo {
        USB("usb"),
        PRODUCT("product"),
        MODEL("model"),
        TRANSPORT_ID("transport_id"),
        DEVICE("device");

        private String info;

        DeviceInfo(String info) {
            this.info = info;
        }

        public String get() {
            return info;
        }

        @Override
        public String toString() {
            return info;
        }
    }

    enum DeviceState {
        ONLINE("device"),
        OFFLINE("offline"),
        UNPLUGGED("no device"),
        NO_PERMISSIONS("no permissions"),
        UNDEFINED(Constants.NULL);

        private String state;
        private static final Map<String, DeviceState> lookup = new HashMap<>();

        static {
            for (DeviceState d : DeviceState.values()) {
                lookup.put(d.get(), d);
            }
        }

        DeviceState(String state) {
            this.state = state;
        }

        public static DeviceState get(String value) {
            return lookup.get(value);
        }

        public String get() {
            return state;
        }

        @Override
        public String toString() {
            return state;
        }
    }
}
