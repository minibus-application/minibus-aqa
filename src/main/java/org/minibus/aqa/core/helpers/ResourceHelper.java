package org.minibus.aqa.core.helpers;

import org.minibus.aqa.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class ResourceHelper {

    private static ResourceHelper instance;
    private final String FOLDER_NAME = "resources";
    private HashMap<String, Path> resourcesMap;
    private HashMap<String, Properties> propertiesMap;

    private ResourceHelper() {
        this.resourcesMap = new HashMap<>();
        this.propertiesMap = new HashMap<>();
    }

    public static synchronized ResourceHelper getInstance(){
        if (instance == null) instance = new ResourceHelper();
        return instance;
    }

    public void initResources() {
        try {
            Files.walk(Paths.get(Constants.PROJECT_DIR_PATH))
                    .filter(path -> Files.isDirectory(path) && path.getFileName().toString().equals(FOLDER_NAME))
                    .forEach(this::seekForFiles);

        } catch (IOException e) {
            throw new RuntimeException("Failed to init resources", e);
        }
    }

    public Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try {
            String fullFileName = resolvePropertiesFileName(fileName);
            properties.load(new FileReader(new File(getResourceFilePath(fullFileName))));
            propertiesMap.put(fullFileName, properties);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName + " property file", e);
        }

        return properties;
    }

    public String getResourceFilePath(String fileName) {
        return resourcesMap.get(fileName).toString();
    }

    public Properties getProperties(String fileName) {
        return propertiesMap.get(resolvePropertiesFileName(fileName));
    }

    private void seekForFiles(Path dir) {
        try {
            Files.walk(dir)
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> resourcesMap.put(filePath.getFileName().toString(), filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to loadProperties " + dir + " file", e);
        }
    }

    private String resolvePropertiesFileName(String fileName) {
        return fileName + ResourceExtension.PROPERTIES.toString();
    }

    private enum ResourceExtension {
        APK(".apk"),
        PROPERTIES(".properties");

        private final String extension;

        ResourceExtension(String extension) {
            this.extension = extension;
        }

        @Override
        public String toString() {
            return extension;
        }
    }
}
