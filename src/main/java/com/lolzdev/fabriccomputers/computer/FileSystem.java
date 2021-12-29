package com.lolzdev.fabriccomputers.computer;

import com.lolzdev.fabriccomputers.api.IFileSystem;
import net.fabricmc.loader.api.FabricLoader;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystem implements IFileSystem {
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

        this.pcPath = pcPath;
        this.mounted = true;
    }

    @Override
    public String readFile(String path) {
        Path filePath = Path.of(this.pcPath.toString(), Path.of(path).toString());

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void writeFile(String path, String content) {
        Path filePath = Path.of(this.pcPath.toString(), Path.of(path).toString());

        try {
            Files.writeString(filePath, content);
        } catch (IOException e) {
            LuaValue error = JsePlatform.standardGlobals().get("error");
            error.call("Failed to read file: " + e.getMessage());
        }
    }

    @Override
    public boolean exists(String path) {
        return new File(Path.of(this.pcPath.toString(), Path.of(path).toString()).toUri()).exists();
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
        return new File(Path.of(this.pcPath.toString(), Path.of(path).toString()).toUri()).mkdir();
    }

    @Override
    public LuaTable list(String path) {
        List<String> entries = Stream.of(new File(Path.of(this.pcPath.toString(), Path.of(path).toString()).toUri()).listFiles())
                .map(File::getName).toList();
        LuaTable result = new LuaTable();
        for (int i=0; i < entries.size(); i++)  {
            result.insert(i+1, LuaValue.valueOf(entries.get(i)));
        }
        return result;
    }



    @Override
    public boolean isDir(String path) {
        return new File(Path.of(this.pcPath.toString(), Path.of(path).toString()).toUri()).isDirectory();
    }

    @Override
    public boolean remove(String path) {
        return new File(Path.of(this.pcPath.toString(), Path.of(path).toString()).toUri()).delete();
    }
}
