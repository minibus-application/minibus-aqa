package org.minibus.aqa.main;


import java.util.Locale;

public class Constants {

    public static final Locale APP_LOCALE = Locale.ENGLISH;
    public static final String APP_TIMEZONE = "Europe/Minsk";

    public static final String PROJECT_DIR_PATH = System.getProperty("user.dir");
    public static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir");
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String PROJECT_TEMP_FOLDER = PROJECT_DIR_PATH + "/target/tmp";
    public static final String PROJECT_REPORT_SCREENSHOT_FOLDER = PROJECT_DIR_PATH + "/target/screenshots";
    public static final String NOT_ASSIGNED = "N/A";
    public static final String PROPERTIES_DELIMITER = ";";
    public static final String PNG = "png";
    public static final String NULL = "null";
    public static final String DEVICE_SDCARD = "/sdcard/";
    public static final String TEMP = "tmp";
}
