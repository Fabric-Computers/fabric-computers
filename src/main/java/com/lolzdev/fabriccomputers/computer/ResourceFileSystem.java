package com.lolzdev.fabriccomputers.computer;

import com.lolzdev.fabriccomputers.api.IFileSystem;
import org.carbon.vm2.LuaTable;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;


public class ResourceFileSystem implements IFileSystem {
    private final String res;
    public Path pcPath;
    boolean mounted;
    public String uuid;

    public ResourceFileSystem(String res) {
        this.res = res;
        this.mounted = false;
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
    public void mount(String id) {
        this.uuid = id;

        this.pcPath = Path.of(this.res);

        this.mounted = true;
    }

    @Override
    public String readFile(String path) {
        InputStream is = this.getClass().getResourceAsStream("/assets/fabriccomputers/lua/".concat(this.res).concat("/").concat(path));

        if (is != null) {

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (is, Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return  textBuilder.toString();
        }
        return null;
    }

    @Override
    public void writeFile(String path, String content) {
    }

    @Override
    public boolean exists(String path) {
        return this.getClass().getResourceAsStream("/assets/fabriccomputers/lua/".concat(this.res).concat("/").concat(path)) != null;
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
        return false;
    }

    @Override
    public LuaTable list(String path) {
        return new LuaTable();
    }

    @Override
    public boolean isDir(String path) {
        return false;
    }

    @Override
    public boolean remove(String path) {
        return false;
    }
}
