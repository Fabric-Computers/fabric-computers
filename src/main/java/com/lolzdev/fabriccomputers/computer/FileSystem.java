package com.lolzdev.fabriccomputers.computer;

import net.fabricmc.loader.api.FabricLoader;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.ast.Str;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class FileSystem implements IFileSystem{
    public Path pcPath;
    boolean mounted;
    public String uuid;

    @Override
    public boolean isMounted() {
        return mounted;
    }

    public FileSystem() {
        this.mounted = false;
    }

    @Override
    public String getUUIDOrRandom () {
        return this.uuid == null ? UUID.randomUUID().toString() : this.uuid;
    }

    @Override
    public void mount(String id) {
        Path path = FabricLoader.getInstance().getGameDir().resolve("fabriccomputers");
        File dir = new File(path.toUri());
        if (!dir.exists()) {
            dir.mkdir();
        }

        this.uuid = id;

        Path pcPath = path.resolve(id);
        dir = new File(pcPath.toUri());
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            Files.writeString(pcPath.resolve("test.lua"), "ciao");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.pcPath = pcPath;
        this.mounted = true;
    }

    @Override
    public String readFile(String path) {
        Path filePath = this.pcPath.resolve(path);

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void writeFile(String path, String content) {
        Path filePath = this.pcPath.resolve(path);

        try {
            Files.writeString(filePath, content);
        } catch (IOException e) {
            LuaValue error = JsePlatform.standardGlobals().get("error");
            error.call("Failed to read file: " + e.getMessage());
        }
    }

    @Override
    public boolean exists(String path) {
        Path filePath = this.pcPath.resolve(path);

        return new File(filePath.toUri()).exists();
    }

    @Override
    public Path getPcPath() {
        return this.pcPath;
    }

    @Override
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean makeDir(String path) {
        Path filePath = this.pcPath.resolve(path);

        File file = new File(filePath.toUri());
        return file.mkdir();
    }
}
