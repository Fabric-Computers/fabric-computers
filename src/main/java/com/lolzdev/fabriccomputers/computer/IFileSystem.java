package com.lolzdev.fabriccomputers.computer;

import org.luaj.vm2.LuaTable;

import java.nio.file.Path;

public interface IFileSystem {
    String mountFs(IFileSystem fs);
    void unmountFs(String fs);
    LuaTable getFilesystems();
    String getUUIDOrRandom ();
    boolean isMounted();
    void mount(String id);
    String readFile(String path);
    void writeFile(String path, String content);
    boolean exists(String path);
    Path getPcPath();
    void setUUID(String uuid);
}
