package org.minibus.aqa.core.common.handlers;

public enum DeviceType {

    EMULATOR("Android Emulator"),
    PHYSICAL("Physical Device");

    private final String desc;

    DeviceType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
