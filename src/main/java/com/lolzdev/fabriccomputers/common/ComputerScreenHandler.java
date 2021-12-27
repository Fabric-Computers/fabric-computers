package com.lolzdev.fabriccomputers.common;

import com.lolzdev.fabriccomputers.FabricComputers;
import com.lolzdev.fabriccomputers.blockentities.ComputerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class ComputerScreenHandler extends ScreenHandler {

    ComputerBlockEntity computer;

    public ComputerScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory);
    }

    public ComputerScreenHandler(int syncId, PlayerInventory inventory, ComputerBlockEntity computer) {
        this(syncId, inventory);

        this.computer = computer;
        this.computer.players.add(inventory.player);
    }

    public ComputerScreenHandler(int syncId, PlayerInventory inventory) {
        super(FabricComputers.COMPUTER_SCREEN_HANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);

        if (this.computer != null) {
            this.computer.players.remove(player);
        }
    }

    public void keyDown(int keyCode) {
        if (this.computer != null) {
            this.computer.computer.queueEvent("key_down", new Object[] {keyCode});
        }
    }
}
