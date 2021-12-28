package com.lolzdev.fabriccomputers.common;

import com.lolzdev.fabriccomputers.FabricComputers;
import com.lolzdev.fabriccomputers.blockentities.ComputerBlockEntity;
import com.lolzdev.fabriccomputers.blockentities.ScreenComponentBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class ScreenComponentScreenHandler extends ScreenHandler {
    private ScreenComponentBlockEntity screen;

    public ScreenComponentScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory);
    }

    public ScreenComponentScreenHandler(int syncId, PlayerInventory inventory, ScreenComponentBlockEntity screen) {
        this(syncId, inventory);

        this.screen = screen;
        this.screen.players.add(inventory.player);
    }

    public ScreenComponentScreenHandler(int syncId, PlayerInventory inventory) {
        super(FabricComputers.SCREEN_COMPONENT_SCREEN_HANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);

        if (this.screen != null) {
            this.screen.players.remove(player);
        }
    }

    public void keyUp(int keyCode) {
        if (this.screen != null) {
            this.screen.keyUp(keyCode);

            this.screen.queueEvent("key_up", new Object[] {keyCode});
        }
    }

    public void keyDown(int keyCode) {
        if (this.screen != null) {
            this.screen.keyDown(keyCode);

            this.screen.queueEvent("key_down", new Object[] {keyCode});
        }
    }
}
