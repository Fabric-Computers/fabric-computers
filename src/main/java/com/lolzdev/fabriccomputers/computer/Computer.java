package com.lolzdev.fabriccomputers.computer;

import com.lolzdev.fabriccomputers.api.IComponent;
import com.lolzdev.fabriccomputers.blockentities.ComputerBlockEntity;
import com.lolzdev.fabriccomputers.blockentities.DiskDriveBlockEntity;
import com.lolzdev.fabriccomputers.common.packets.PixelBufferChangePacket;
import com.lolzdev.fabriccomputers.computer.luaj.ComputerDebugLib;
import com.lolzdev.fabriccomputers.items.FloppyDiskItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.chunk.WorldChunk;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

public class Computer {

    public boolean shouldUpdate = true;
    public FileSystem fs;
    public UUID id;
    public boolean needSetup;
    public boolean halted;
    private final Queue<Event> queueEvents;
    private Thread executor;
    private ComputerBlockEntity blockEntity;
    public boolean interrupted;



    public Computer(ComputerBlockEntity blockEntity) {
        this.fs = new FileSystem();

        this.halted = true;
        this.needSetup = true;
        this.queueEvents = new ArrayDeque<>(4);
        this.blockEntity = blockEntity;
        this.interrupted = true;
    }

    public void reboot() {
        this.shudown();
        this.boot();
    }

    public void shudown() {
        this.interrupted = true;

        this.halted = true;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getId() {
        return this.id.toString();
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

        if (this.id == null) {
            this.setId(UUID.randomUUID());
        }

        fs = new FileSystem();

        this.needSetup = false;
    }

    public void boot() {

        System.out.println("Booting");
        this.interrupted = false;

        this.loadBios();
        this.halted = false;
    }

    public void sleep(int nanos) {
        long startTime = System.nanoTime();
        while(System.nanoTime() - startTime < nanos){}
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

    public void loadBios() {

        this.executor = new Thread(() -> {
            try {
                Globals globals = JsePlatform.debugGlobals();
                globals.load(new ComputerDebugLib(this));
                globals.set("computer", CoerceJavaToLua.coerce(this));
                globals.set("fs", CoerceJavaToLua.coerce(this.fs));
                LuaValue chunk = globals.load(Files.readString(Path.of(this.getClass().getResource("/assets/fabriccomputers/rom/bios.lua").toURI())));

                try {
                    chunk.call();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        this.executor.start();

    }
}
