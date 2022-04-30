package com.lolzdev.fabriccomputers.computer;

import com.lolzdev.fabriccomputers.blockentities.ComputerBlockEntity;
import com.lolzdev.fabriccomputers.cpu.riscv.Rv64;

public class Computer {
    /*

    public boolean shouldUpdate = true;
    public FileSystem fs;
    public UUID id;
    public boolean needSetup;
    public boolean halted;
    private final Queue<Event> queueEvents;
    public Thread executor;
    private ComputerBlockEntity blockEntity;
    public boolean interrupted;
    Globals globals;

    public boolean crazyBitHack(int value, int offset) {
        return ((value >> offset) & 1) == 1;
    }
    public Computer(ComputerBlockEntity blockEntity) {
        this.fs = new FileSystem();

        this.halted = true;
        this.needSetup = true;
        this.queueEvents = new LinkedBlockingDeque<>(4);
        this.blockEntity = blockEntity;
        this.interrupted = true;
        this.globals = JsePlatform.debugGlobals();
    }

    public void reboot() {
        this.shutdown();
        this.boot();
    }

    public void shutdown() {
        this.globals.interrupt();

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
        if (this.queueEvents.size() > 1000) {
            for (int i=0; i < 1000; i++) {
                this.queueEvents.poll();
            }
        }
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

        this.globals = JsePlatform.debugGlobals();
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
                    return entity.getComponent();
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
                globals.set("computer", CoerceJavaToLua.coerce(this));
                globals.set("fs", CoerceJavaToLua.coerce(this.fs));
                LuaValue chunk = globals.load(Files.readString(Path.of(this.getClass().getResource("/assets/fabriccomputers/rom/bios.lua").toURI())), "bios.lua");

                chunk.call();


            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        this.executor.start();

    }

    /**
     * This would ideally be split into parts, and moved to some math lib for lua to use
     * currently this is needed for the font rasterizer in fos/lib/io.lua
     * Doing this in lua it very inefficient as that language (for some reason) doesn't have bit operations
     *
     * @param value - font row data encoded as an int value where each bit represents a pixel
     * @param offset - the offset into the pixel data row
     *
     * @return true is the pixel should be set, and false otherwise
     */

    public Rv64 cpu;
    public Drive drive;
    ComputerBlockEntity blockEntity;

    public boolean halted = true;

    public Computer(ComputerBlockEntity blockEntity) {
        this.cpu = new Rv64();
        this.blockEntity = blockEntity;
    }

    public Drive getDrive() {
        return this.drive;
    }

    public void shutdown() {
        this.halted = true;
        this.cpu.halted = true;
    }

    public void boot() {
        Thread executor = new Thread(() -> cpu.boot(drive.content));

        executor.start();
    }
}
