package org.minibus.aqa.main.core.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minibus.aqa.main.Constants;
import org.minibus.aqa.main.core.cli.ShellCommandResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceHelper {
    private static final Logger LOGGER = LogManager.getLogger(ResourceHelper.class);
    private static ResourceHelper instance;
    private List<Path> resources;

    private ResourceHelper(LinkedList<Path> resources) {
        this.resources = resources;
    }

    public static synchronized ResourceHelper getInstance() {
        if (instance == null) {
            instance = new ResourceHelper(initResources());
        }
        return instance;
    }

    public Path getResourcePath(String fullFileName, String... precedingPath) {
        final String desiredPath = StringUtils.join(precedingPath, File.separator);
        Optional<Path> optPath = resources.stream()
                .filter(p -> p.getFileName().toString().equals(fullFileName))
                .filter(p -> {
                    if (desiredPath.length() > 0) {
                        int endDirIndex = p.getNameCount() - 1;
                        int startDirIndex = endDirIndex - precedingPath.length;
                        return p.subpath(startDirIndex, endDirIndex).toString().endsWith(desiredPath);
                    } else return true;
                })
                .findFirst();
        return optPath.orElseThrow(() -> {
            throw new RuntimeException(String.format("Can not extract '%s' file with '%s' path from paths:\n%s",
                    fullFileName, desiredPath,
                    StringUtils.join(resources.stream().map(Path::toString).collect(Collectors.toList()), "\n")));
        });
    }

    private static LinkedList<Path> initResources() {
        final String RESOURCES = "resources";
        LinkedList<Path> allResourceFilePaths = new LinkedList<>();
        try {
            Files.walk(Paths.get(Constants.PROJECT_DIR_PATH))
                    .filter(dir -> Files.isDirectory(dir) && dir.getFileName().toString().equals(RESOURCES))
                    .forEach(resourcesDirPath -> {
                        allResourceFilePaths.addAll(pullOutFiles(resourcesDirPath));
                    });
            return allResourceFilePaths;
        } catch (IOException e) {
            throw new RuntimeException("Failed to init resources\n", e);
        }
    }

    private static LinkedList<Path> pullOutFiles(Path resourcesDir) {
        LinkedList<Path> paths = new LinkedList<>();

        try {
            Files.walkFileTree(resourcesDir, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isRegularFile()) {
                        paths.add(filePath);
                    }

                    return super.visitFile(filePath, attrs);
                }
            });
            return paths;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties in " + resourcesDir + "\n", e);
        }
    }
}
