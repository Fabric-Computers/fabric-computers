package com.lolzdev.fabriccomputers.api;

import java.nio.file.Path;

public interface IFileSystem {
    String getUUIDOrRandom ();
    boolean isMounted();
    void mount(String id);
    String readFile(String path);
    void writeFile(String path, String content);
    boolean exists(String path);
    Path getPcPath();
    void setUUID(String uuid);
    boolean makeDir(String path);
}
