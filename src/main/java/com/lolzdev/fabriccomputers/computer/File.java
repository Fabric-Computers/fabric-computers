package com.lolzdev.fabriccomputers.computer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class File {
    private Path path;

    public File(Path path) {
        this.path = path;
    }

    public String readAll() {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
