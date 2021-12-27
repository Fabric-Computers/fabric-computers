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

public class FileSystem {
    public Path pcPath;
    private boolean mounted;
    public final HashMap<String, FileSystem> mountedFs;
    public String uuid;

    public boolean isMounted() {
        return mounted;
    }

    public FileSystem() {
        this.mounted = false;
        this.mountedFs = new HashMap<>();
    }

    public String getUUIDOrRandom () {
        return this.uuid == null ? UUID.randomUUID().toString() : this.uuid;
    }

    public LuaTable getFilesystems() {
        LuaTable table = new LuaTable();

        for (String key : this.mountedFs.keySet()) {
            table.set(key, CoerceJavaToLua.coerce(this.mountedFs.get(key)));
        }

        return table;
    }

    public String mountFs(FileSystem fs) {
        String fsName = String.format("storage%d", this.mountedFs.size());

        int c = 0;
        for (String i : this.mountedFs.keySet()) {
            if (fs.pcPath == this.mountedFs.get(i).pcPath) {
                return String.format("storage%d", c);
            }
            c++;
        }
        LuaTable table = new LuaTable();

        this.mountedFs.put(fsName, fs);

        return fsName;
    }

    public void unmountFs(String fs) {
        this.mountedFs.remove(fs);
    }

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

    public String readFile(String path) {
        if (path.startsWith("storage")) {
            if (this.mountedFs.containsKey(path.split("/")[0])) {
                return this.mountedFs.get(path.split("/")[0]).readFile(path.replace(path.split("/")[0], ""));
            }
        }

        Path filePath = this.pcPath.resolve(path);

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return null;
        }
    }

    public void writeFile(String path, String content) {
        if (path.startsWith("storage")) {
            System.out.println(path.split("/")[0]);
            if (this.mountedFs.containsKey(path.split("/")[0])) {
                this.mountedFs.get(path.split("/")[0]).writeFile(path.replace(path.split("/")[0], ""), content);
                return;
            }
        }

        Path filePath = this.pcPath.resolve(path);

        try {
            Files.writeString(filePath, content);
        } catch (IOException e) {
            LuaValue error = JsePlatform.standardGlobals().get("error");
            error.call("Failed to read file: " + e.getMessage());
        }
    }

    public boolean exists(String path) {
        Path filePath = this.pcPath.resolve(path);

        return new File(filePath.toUri()).exists();
    }
}
