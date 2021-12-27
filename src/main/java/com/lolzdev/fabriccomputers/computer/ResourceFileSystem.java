package com.lolzdev.fabriccomputers.computer;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ResourceFileSystem implements IFileSystem {
    private final String res;
    public Path pcPath;
    boolean mounted;
    public final HashMap<String, IFileSystem> mountedFs;
    public String uuid;

    public ResourceFileSystem(String res) {
        this.res = res;
        this.mounted = false;
        this.mountedFs = new HashMap<>();
    }

    @Override
    public boolean isMounted() {
        return mounted;
    }

    @Override
    public String getUUIDOrRandom () {
        return this.uuid == null ? UUID.randomUUID().toString() : this.uuid;
    }

    @Override
    public LuaTable getFilesystems() {
        LuaTable table = new LuaTable();

        for (String key : this.mountedFs.keySet()) {
            table.set(key, CoerceJavaToLua.coerce(this.mountedFs.get(key)));
        }

        return table;
    }

    @Override
    public String mountFs(IFileSystem fs) {
        String fsName = String.format("storage%d", this.mountedFs.size());

        int c = 0;
        for (String i : this.mountedFs.keySet()) {
            if (fs.getPcPath() == this.mountedFs.get(i).getPcPath()) {
                return String.format("storage%d", c);
            }
            c++;
        }
        LuaTable table = new LuaTable();

        this.mountedFs.put(fsName, fs);

        return fsName;
    }

    @Override
    public void unmountFs(String fs) {

        for (String key : this.mountedFs.keySet()) {
            if (Objects.equals(this.mountedFs.get(key).getUUIDOrRandom(), fs)) {
                this.mountedFs.remove(fs);
            }
        }
    }

    @Override
    public void mount(String id) {
        Path path = FabricLoader.getInstance().getGameDir().resolve("fabriccomputers");
        java.io.File dir = new java.io.File(path.toUri());
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

        try {
            FileUtils.copyDirectory(new File(this.getClass().getResource("/assets/fabriccomputers/lua/" + this.res).toURI()), new File(this.pcPath.toUri()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }


        this.mounted = true;
    }

    @Override
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

    @Override
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
}
