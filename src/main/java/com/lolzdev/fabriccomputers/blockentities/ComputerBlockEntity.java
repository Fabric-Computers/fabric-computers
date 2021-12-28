package com.lolzdev.fabriccomputers.blockentities;

import com.lolzdev.fabriccomputers.common.ComputerScreenHandler;
import com.lolzdev.fabriccomputers.common.PixelBufferChangePacket;
import com.lolzdev.fabriccomputers.computer.Computer;
import com.lolzdev.fabriccomputers.items.FixedFloppyDiskItem;
import com.lolzdev.fabriccomputers.items.FloppyDiskItem;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComputerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    public Computer computer;
    public List<PlayerEntity> players;

    public ComputerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.COMPUTER_BLOCK_ENTITY, pos, state);

        this.computer = new Computer(this);
        this.players = new ArrayList<>();
    }

    public static void tick(ComputerBlockEntity blockEntity) {

        if (blockEntity.world != null && !blockEntity.world.isClient()) {
            if(blockEntity.computer.needSetup) {
                blockEntity.computer.setup();
            }
            if(blockEntity.computer.isKeyDown(341) && blockEntity.computer.isKeyDown(82)) {
                blockEntity.computer.reboot();
            }

            for (PlayerEntity player : blockEntity.players) {
                PixelBufferChangePacket.send(player, blockEntity.computer.changes[0], blockEntity.computer.changes[1], blockEntity.computer.changes[2], blockEntity.computer.changes[3], blockEntity.computer.getPixelBufferAsInt(), blockEntity.computer.shouldUpdate);
            }
            blockEntity.computer.update();
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Computer");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ComputerScreenHandler(syncId, inv, this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {}

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("uuid")) {
            this.computer.setId(UUID.fromString(nbt.getString("uuid")));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putString("uuid", this.computer.id.toString());
    }
}