package com.lolzdev.fabriccomputers.computer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceManager;
import org.apache.commons.io.FileUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipInputStream;

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
        this.uuid = id;

        this.pcPath = Path.of(this.res);


        /*
        Path path = FabricLoader.getInstance().getGameDir().resolve("fabriccomputers");
        java.io.File dir = new java.io.File(path.toUri());
        if (!dir.exists()) {
            dir.mkdir();
        }

        Path pcPath = path.resolve(id);
        dir = new File(pcPath.toUri());
        if (!dir.exists()) {
            dir.mkdir();
        }

        this.pcPath = pcPath;


        try {
            File file = new File(this.pcPath.toUri());
            if (!file.exists()) {
                file.createNewFile();
            }
            FileUtils.copyInputStreamToFile(this.getClass().getResource("/assets/fabriccomputers/lua/" + this.res).openStream(), new File(this.pcPath.toUri()).createNewFile());
            FileUtils.copyDirectory(new File(this.getClass().getResource("/assets/fabriccomputers/lua/" + this.res).toURI()), new File(this.pcPath.toUri()));
        } catch (IOException e) {
            e.printStackTrace();
        }

            this.pcPath = path;
         */




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
}
