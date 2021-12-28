package com.lolzdev.fabriccomputers.computer;

import com.lolzdev.fabriccomputers.api.IComponent;
import com.lolzdev.fabriccomputers.blockentities.ComputerBlockEntity;
import com.lolzdev.fabriccomputers.blockentities.DiskDriveBlockEntity;
import com.lolzdev.fabriccomputers.items.FloppyDiskItem;
import jdk.jfr.Timespan;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.ast.Str;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

public class Computer {
    public int[][] pixels = new int[245][177];
    public int[] changes = new int[] {0, 0, 0, 0};
    public boolean shouldUpdate = true;
    public FileSystem fs;
    public UUID id;
    public boolean needSetup;
    public boolean halted;
    private final Queue<Event> queueEvents;
    private Thread executor;
    private final HashMap<Integer, Boolean> keyCodes;
    private ComputerBlockEntity blockEntity;

    public Computer(ComputerBlockEntity blockEntity) {

        this.fs = new FileSystem();

        this.halted = true;
        this.needSetup = true;
        this.queueEvents = new ArrayDeque<>(4);
        this.keyCodes = new HashMap<>();
        this.blockEntity = blockEntity;
    }

    public void reboot() {
        if (this.executor.isAlive()) {
            this.queueEvent("interrupt", new Object[]{});
        }
        this.halted = true;
        this.boot();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getId() {
        return this.id.toString();
    }

    public LuaTable getScreenSize() {
        LuaTable size = new LuaTable();
        size.set(1, LuaValue.valueOf(245));
        size.set(2, LuaValue.valueOf(177));
        return size;
    }

    public void keyDown(int keyCode) {
        this.keyCodes.put(keyCode, true);
    }

    public void keyUp(int keyCode) {
        this.keyCodes.replace(keyCode, false);
    }

    public boolean isKeyDown(int keyCode) {
        return this.keyCodes.getOrDefault(keyCode, false);
    }

    public void queueEvent(String name, Object[] args) {
        this.queueEvents.offer(new Event(name, args));
    }

    public LuaTable pollEvent() {
        Event event = this.queueEvents.poll();
        if (event != null) {
            LuaTable value = new LuaTable();
            value.set(1, LuaValue.valueOf(event.name));
            if (event.args != null) {
                for (int i=1; i < event.args.length+1; i++) {
                    if (event.args[i-1] instanceof String) {
                        value.set(i+1, LuaValue.valueOf((String) event.args[i-1]));
                    } else if (event.args[i-1] instanceof Integer) {
                        value.set(i+1, LuaValue.valueOf((Integer) event.args[i-1]));
                    } else if (event.args[i-1] instanceof Boolean) {
                        value.set(i+1, LuaValue.valueOf((Boolean) event.args[i-1]));
                    } else if (event.args[i-1] instanceof Double) {
                        value.set(i+1, LuaValue.valueOf((Double) event.args[i-1]));
                    }
                }
            }
            return value;
        }

        return null;
    }

    public void setup() {
        for (int x = 0; x < 245; x++) {
            for (int y = 0; y < 177; y++) {
                setPixel(x, y, 0x000000);
            }
        }

        if (this.id == null) {
            this.setId(UUID.randomUUID());
        }

        fs = new FileSystem();

        this.needSetup = false;
    }

    public void boot() {
        this.loadBios();
        this.halted = false;
    }

    public void setPixel(int x, int y, int color) {

        if (x <= 244 && y <= 176) {
            int rgba = (((color) & 0xFF) << 16)
                    | (((color >> 8) & 0xFF) << 8)
                    | (((color >> 16) & 0xFF))
                    | (((25 & 0xFF) << 24));

            this.pixels[x][y] = rgba;

            if (x < this.changes[0]) {
                this.changes[0] = x;
            }
            if (x > this.changes[1]) {
                this.changes[1] = x;
            }

            if (y < this.changes[2]) {
                this.changes[2] = y;
            }
            if (y > this.changes[3]) {
                this.changes[3] = y;
            }

            this.shouldUpdate = true;
        }
    }

    public void sleep(int nanos) {
        if (this.executor != null) {
            try {
                this.executor.wait(0, nanos);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public LuaValue getComponent(int index) {

        switch (index) {
            case 0 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(1, 0, 0)).getBlockEntity(this.blockEntity.getPos().add(1, 0, 0), WorldChunk.CreationType.IMMEDIATE) instanceof IComponent entity)  {
                    return  entity.getComponent();
                }
            }

            case 1 -> {

                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(-1, 0, 0)).getBlockEntity(this.blockEntity.getPos().add(-1, 0, 0), WorldChunk.CreationType.IMMEDIATE) instanceof IComponent entity)  {
                    return  entity.getComponent();
                }
            }

            case 2 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 0, 1)).getBlockEntity(this.blockEntity.getPos().add(0, 0, 1), WorldChunk.CreationType.IMMEDIATE) instanceof IComponent entity)  {
                    return  entity.getComponent();
                }
            }

            case 3 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 0, -1)).getBlockEntity(this.blockEntity.getPos().add(0, 0, -1), WorldChunk.CreationType.IMMEDIATE) instanceof IComponent entity)  {
                    return  entity.getComponent();
                }
            }

            case 4 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 1, 0)).getBlockEntity(this.blockEntity.getPos().add(0, 1, 0), WorldChunk.CreationType.IMMEDIATE) instanceof IComponent entity)  {
                    return  entity.getComponent();
                }
            }

            case 5 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, -1, 0)).getBlockEntity(this.blockEntity.getPos().add(0, -1, 0), WorldChunk.CreationType.IMMEDIATE) instanceof IComponent entity)  {
                    return  entity.getComponent();
                }
            }
        }

        return LuaValue.NIL;
    }

    public LuaValue getFloppyFs(int index) {
        switch (index) {
            case 0 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(1, 0, 0)).getBlockEntity(this.blockEntity.getPos().add(1, 0, 0), WorldChunk.CreationType.IMMEDIATE) instanceof DiskDriveBlockEntity)  {
                    DiskDriveBlockEntity entity = (DiskDriveBlockEntity) this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(1, 0, 0)).getBlockEntity(this.blockEntity.getPos().add(1, 0, 0), WorldChunk.CreationType.IMMEDIATE);

                    if (entity != null) {
                        if (entity.getItems().get(0).getItem() instanceof FloppyDiskItem disk) {
                            return CoerceJavaToLua.coerce(disk.fileSystem);
                        }
                    }
                }
            }

            case 1 -> {

                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(-1, 0, 0)).getBlockEntity(this.blockEntity.getPos().add(-1, 0, 0), WorldChunk.CreationType.IMMEDIATE) instanceof DiskDriveBlockEntity)  {
                    DiskDriveBlockEntity entity = (DiskDriveBlockEntity) this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(-1, 0, 0)).getBlockEntity(this.blockEntity.getPos().add(-1, 0, 0), WorldChunk.CreationType.IMMEDIATE);

                    if (entity != null) {
                        if (entity.getItems().get(0).getItem() instanceof FloppyDiskItem disk) {
                            return CoerceJavaToLua.coerce(disk.fileSystem);
                        }
                    }
                }
            }

            case 2 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 0, 1)).getBlockEntity(this.blockEntity.getPos().add(0, 0, 1), WorldChunk.CreationType.IMMEDIATE) instanceof DiskDriveBlockEntity)  {
                    DiskDriveBlockEntity entity = (DiskDriveBlockEntity) this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 0, 1)).getBlockEntity(this.blockEntity.getPos().add(0, 0, 1), WorldChunk.CreationType.IMMEDIATE);

                    if (entity != null) {
                        if (entity.getItems().get(0).getItem() instanceof FloppyDiskItem disk) {
                            return CoerceJavaToLua.coerce(disk.fileSystem);
                        }
                    }
                }
            }

            case 3 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 0, -1)).getBlockEntity(this.blockEntity.getPos().add(0, 0, -1), WorldChunk.CreationType.IMMEDIATE) instanceof DiskDriveBlockEntity)  {
                    DiskDriveBlockEntity entity = (DiskDriveBlockEntity) this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 0, -1)).getBlockEntity(this.blockEntity.getPos().add(0, 0, -1), WorldChunk.CreationType.IMMEDIATE);

                    if (entity != null) {
                        if (entity.getItems().get(0).getItem() instanceof FloppyDiskItem disk) {
                            return CoerceJavaToLua.coerce(disk.fileSystem);
                        }
                    }
                }
            }

            case 4 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 1, 0)).getBlockEntity(this.blockEntity.getPos().add(0, 1, 0), WorldChunk.CreationType.IMMEDIATE) instanceof DiskDriveBlockEntity)  {
                    DiskDriveBlockEntity entity = (DiskDriveBlockEntity) this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, 1, 0)).getBlockEntity(this.blockEntity.getPos().add(0, 1, 0), WorldChunk.CreationType.IMMEDIATE);

                    if (entity != null) {
                        if (entity.getItems().get(0).getItem() instanceof FloppyDiskItem disk) {
                            return CoerceJavaToLua.coerce(disk.fileSystem);
                        }
                    }
                }
            }

            case 5 -> {
                if (this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, -1, 0)).getBlockEntity(this.blockEntity.getPos().add(0, -1, 0), WorldChunk.CreationType.IMMEDIATE) instanceof DiskDriveBlockEntity)  {
                    DiskDriveBlockEntity entity = (DiskDriveBlockEntity) this.blockEntity.getWorld().getWorldChunk(this.blockEntity.getPos().add(0, -1, 0)).getBlockEntity(this.blockEntity.getPos().add(0, -1, 0), WorldChunk.CreationType.IMMEDIATE);

                    if (entity != null) {
                        if (entity.getItems().get(0).getItem() instanceof FloppyDiskItem disk) {
                            return CoerceJavaToLua.coerce(disk.fileSystem);
                        }
                    }
                }
            }
        }

        return LuaValue.NIL;
    }

    public void update() {
        this.shouldUpdate = false;
    }

    public int[] getPixelBufferAsInt() {
        int[] result = new int[245 * 177];

        int cur = 0;
        for (int x = 0; x < 245; x++) {
            for (int y = 0; y < 177; y++) {
                result[cur] = pixels[x][y];

                cur++;
            }
        }

        return result;
    }

    public void loadBios() {
        this.executor = new Thread(() -> {
            try {
                Globals globals = JsePlatform.standardGlobals();
                globals.set("computer", CoerceJavaToLua.coerce(this));
                globals.set("fs", CoerceJavaToLua.coerce(this.fs));
                globals.load(Files.readString(Path.of(this.getClass().getResource("/assets/fabriccomputers/rom/bios.lua").toURI()))).call();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        this.executor.start();

    }
}
