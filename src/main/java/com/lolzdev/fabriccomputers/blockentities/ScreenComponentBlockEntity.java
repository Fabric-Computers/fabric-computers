package com.lolzdev.fabriccomputers.blockentities;

import com.lolzdev.fabriccomputers.api.IComponent;
import com.lolzdev.fabriccomputers.common.ScreenComponentScreenHandler;
import com.lolzdev.fabriccomputers.common.packets.PixelBufferChangePacket;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScreenComponentBlockEntity extends BlockEntity implements IComponent, ExtendedScreenHandlerFactory {
    String uuid;
    public List<PlayerEntity> players;
    public final int screenWidth, screenHeight;
    private final int[][] pixels;
    public int changesStartX, changesStartY, changesEndX, changesEndY;
    private final HashMap<Integer, Boolean> keyCodes;
    private boolean shouldUpdate;

    public ComputerBlockEntity getComputerAtIndex(int index) {
        switch (index) {
            case 0 -> {
                if (this.getWorld().getWorldChunk(this.getPos().add(1, 0, 0)).getBlockEntity(this.getPos().add(1, 0, 0), WorldChunk.CreationType.IMMEDIATE) instanceof ComputerBlockEntity entity)  {
                    return entity;
                }
            }

            case 1 -> {

                if (this.getWorld().getWorldChunk(this.getPos().add(-1, 0, 0)).getBlockEntity(this.getPos().add(-1, 0, 0), WorldChunk.CreationType.IMMEDIATE) instanceof ComputerBlockEntity entity)  {
                    return  entity;
                }
            }

            case 2 -> {
                if (this.getWorld().getWorldChunk(this.getPos().add(0, 0, 1)).getBlockEntity(this.getPos().add(0, 0, 1), WorldChunk.CreationType.IMMEDIATE) instanceof ComputerBlockEntity entity)  {
                    return  entity;
                }
            }

            case 3 -> {
                if (this.getWorld().getWorldChunk(this.getPos().add(0, 0, -1)).getBlockEntity(this.getPos().add(0, 0, -1), WorldChunk.CreationType.IMMEDIATE) instanceof ComputerBlockEntity entity)  {
                    return  entity;
                }
            }

            case 4 -> {
                if (this.getWorld().getWorldChunk(this.getPos().add(0, 1, 0)).getBlockEntity(this.getPos().add(0, 1, 0), WorldChunk.CreationType.IMMEDIATE) instanceof ComputerBlockEntity entity)  {
                    return  entity;
                }
            }

            case 5 -> {
                if (this.getWorld().getWorldChunk(this.getPos().add(0, -1, 0)).getBlockEntity(this.getPos().add(0, -1, 0), WorldChunk.CreationType.IMMEDIATE) instanceof ComputerBlockEntity entity)  {
                    return  entity;
                }
            }
        }

        return null;

    }

    public static void tick(ScreenComponentBlockEntity blockEntity) {
        if (blockEntity.world != null && !blockEntity.world.isClient()) {
            for (int i=0; i < 6; i++) {
                ComputerBlockEntity entity = blockEntity.getComputerAtIndex(i);
                if (entity != null) {

                    if (blockEntity.isKeyDown(341) && blockEntity.isKeyDown(82)) {
                        System.out.println("Rebooting");
                        entity.computer.reboot();
                    }
                }
            }

            blockEntity.update();
        }
    }

    public void queueEvent(String name, Object[] args) {
        for (int i=0; i < 6; i++) {
            ComputerBlockEntity computer = this.getComputerAtIndex(i);
            if (computer != null) {
                computer.computer.queueEvent(name, args);
            }
        }
    }

    public ScreenComponentBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SCREEN_COMPONENT_BLOCK_ENTITY, pos, state);

        this.shouldUpdate = true;
        this.screenWidth = 245;
        this.screenHeight = 177;
        this.pixels = new int[screenWidth][screenHeight];
        this.keyCodes = new HashMap<>();
        this.players = new ArrayList<>();

        for (int x = 0; x < this.screenWidth; x++) {
            for (int y = 0; y < this.screenHeight; y++) {
                pixels[x][y] = 0x0000FF;
            }
        }

        this.uuid = UUID.randomUUID().toString();
    }

    public void setPixel(int x, int y, int color) {
        if (x >= 0 && x < screenWidth && y >= 0 && y < screenHeight) {
            pixels[x][y] = (((color) & 0xFF) << 16)
                    | (((color >> 8) & 0xFF) << 8)
                    | (((color >> 16) & 0xFF))
                    | (((25 & 0xFF) << 24));

            if (x < changesStartX) changesStartX = x;
            else if (x > changesEndX) changesEndX = x;

            if (y < changesStartY) changesStartY = y;
            else if (y > changesEndY) changesEndY = y;

            shouldUpdate = true;
        }
    }

    public int[] getPixelBuffer() {
        int[] buffer = new int[screenWidth * screenHeight];

        for (int x = 0; x < screenWidth; x++) {
            System.arraycopy(pixels[x], 0, buffer, screenHeight * x, screenHeight);
        }

        return buffer;
    }

    public void update() {
        if (shouldUpdate && this.players.size() > 0) {
            int[] buffer = getPixelBuffer();

            for (PlayerEntity player : this.players) {
                PixelBufferChangePacket.send(player, buffer, changesStartX, changesStartY, changesEndX, changesEndY);
            }
        }

        changesStartX = changesStartY = changesEndX = changesEndY = 0;
        shouldUpdate = false;
    }

    public LuaTable getScreenSize() {
        LuaTable size = new LuaTable();
        size.set(1, LuaValue.valueOf(screenWidth));
        size.set(2, LuaValue.valueOf(screenHeight));
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

    @Override
    public String getComponentType() {
        return "screen";
    }

    @Override
    public String getComponentUUID() {
        return this.uuid;
    }

    @Override
    public LuaValue getComponent() {
        return CoerceJavaToLua.coerce(this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {

    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("block.fabriccomputers.screen_component");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ScreenComponentScreenHandler(syncId, inv, this);
    }
}
