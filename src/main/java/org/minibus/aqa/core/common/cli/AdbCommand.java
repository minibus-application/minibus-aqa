package org.minibus.aqa.core.common.cli;

public interface AdbCommand {

    String ADB = "adb";
    String SHELL = "shell";
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
        UNDEFINED("null");

        private String state;

        DeviceState(String state) {
            this.state = state;
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
