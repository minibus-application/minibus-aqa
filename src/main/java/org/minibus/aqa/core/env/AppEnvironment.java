package org.minibus.aqa.core.env;

public enum AppEnvironment {
    DEV("dev"),
    STAGE("stage");

    private final String type;

    AppEnvironment(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
