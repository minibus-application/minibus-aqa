package org.minibus.aqa.core.helpers;

import org.minibus.aqa.Constants;
import org.testng.util.Strings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceHelper {

    private static ResourceHelper instance;
    private final String DIR = "resources";
    private List<Path> resources;
    private HashMap<Path, Properties> propertiesMap;

    private ResourceHelper() {
        this.resources = new LinkedList<>();
        this.propertiesMap = new HashMap<>();
    }

    public static synchronized ResourceHelper getInstance() {
        if (instance == null) {
            instance = new ResourceHelper();
        }
        return instance;
    }

    public void initResources(String... exceptions) {
        try {
            Files.walk(Paths.get(Constants.PROJECT_DIR_PATH))
                    .filter(dir -> Files.isDirectory(dir) && dir.getFileName().toString().equals(DIR))
                    .forEach(dir -> pullOutFiles(dir, exceptions));

        } catch (IOException e) {
            throw new RuntimeException("Failed to init resources\n", e);
        }
    }

    public Properties loadProperties(String parentDir, String fileName) {
        Properties properties = new Properties();
        try {
            Path propertiesPath = getResourcePath(parentDir, resolvePropertiesFileName(fileName));
            properties.load(new FileReader(new File(propertiesPath.toString())));
            propertiesMap.put(propertiesPath, properties);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to pull ../%s/%s file. File not found\n", parentDir, fileName), e);
        }

        return properties;
    }

    public Properties loadProperties(String fileName) {
        return loadProperties(DIR, fileName);
    }

    public String getPropertyValue(File propertyFile, String key) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertyFile));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to load properties from %s file. File not found\n", propertyFile), e);
        }
        String value = properties.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            throw new RuntimeException(String.format("Property '%s' not found in file '%s'", key, propertyFile));
        }
        return value;
    }

    public Path getResourcePath(String parentDir, String fileName) {
        try {
            return resources.stream()
                    .filter(p -> p.getFileName().toString().equals(fileName)
                            && p.getParent().getFileName().toString().equals(parentDir))
                    .findFirst()
                    .get();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new RuntimeException(String.format("Failed to pull ../%s/%s file. File not found\n", parentDir, fileName));
        }
    }

    public LinkedList<Path> getResourcePaths(String parentDir) {
        try {
            return resources.stream()
                    .filter(p -> p.getParent().getFileName().toString().equals(parentDir) && Files.isRegularFile(p))
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (NullPointerException e) {
            throw new RuntimeException(String.format("Failed to pull files in ../%s. Dir not found\n", parentDir));
        }
    }

    public File getResourceFile(String parentDir, String fileName) {
        return new File(getResourcePath(parentDir, fileName).toString());
    }

    public Properties getProperties(String parentDir, String fileName) {
        return propertiesMap.get(getResourcePath(parentDir, resolvePropertiesFileName(fileName)));
    }

    public Properties getProperties(Path filePath) {
        return propertiesMap.get(filePath);
    }

    private void pullOutFiles(Path resourcesDir, String... exceptions) {
        try {
            Files.walkFileTree(resourcesDir, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (exceptions.length > 0) {
                        if (Arrays.asList(exceptions).contains(dir.getFileName().toString())) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isRegularFile()) resources.add(filePath);

                    return super.visitFile(filePath, attrs);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties in " + resourcesDir + "\n", e);
        }
    }

    private String resolvePropertiesFileName(String fileName) {
        return fileName != null && fileName.endsWith(".properties")
                ? fileName
                : fileName + ".properties";
    }
}
