package org.entropy.filechannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestFilesCopy {
    public static void main(String[] args) throws IOException {
        Path sourcePath = Paths.get("./src");
        Path targetPath = Paths.get("./backup");

        Files.walk(sourcePath).forEach(path -> {
            try {
                Path targetSubPath = targetPath.resolve(sourcePath.relativize(path));
                if (Files.isDirectory(path)) {
                    Files.createDirectory(targetSubPath);
                } else if (Files.isRegularFile(path)) {
                    Files.copy(path, targetSubPath);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
