package com.lolzdev.fabriccomputers.blockentities;

import com.lolzdev.fabriccomputers.computer.Computer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComputerBlockEntity extends BlockEntity {
    public Computer computer;

    public ComputerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.COMPUTER_BLOCK_ENTITY, pos, state);

        computer = new Computer(this);
    }

    public static void tick(ComputerBlockEntity blockEntity) {

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("uuid")) {this.computer.getDrive().uuid = UUID.fromString(nbt.getString("uuid"));}

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putString("uuid", this.computer.getDrive().uuid.toString());
    }
}