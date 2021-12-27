package com.lolzdev.fabriccomputers.blockentities;

import com.lolzdev.fabriccomputers.common.DiskDriveScreenHandler;
import com.lolzdev.fabriccomputers.utils.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class DiskDriveBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public void diskRemoved(String id) {
        ComputerBlockEntity entity;
        System.out.println("Unmounting: " + id);
        if (this.world.getBlockEntity(this.pos.add(1, 0, 0)) instanceof ComputerBlockEntity)  {
            entity = (ComputerBlockEntity) world.getBlockEntity(this.pos.add(1, 0, 0));

            if (entity.computer != null) {
                entity.computer.queueEvent("disk_unmounted", new Object[] {id});
            }
        }

        if (this.world.getBlockEntity(this.pos.add(-1, 0, 0)) instanceof ComputerBlockEntity)  {
            entity = (ComputerBlockEntity) world.getBlockEntity(this.pos.add(-1, 0, 0));

            if (entity.computer != null) {
                entity.computer.queueEvent("disk_unmounted", new Object[] {id});
            }
        }

        if (this.world.getBlockEntity(this.pos.add(0, 0, 1)) instanceof ComputerBlockEntity)  {
            entity = (ComputerBlockEntity) world.getBlockEntity(this.pos.add(0, 0, 1));

            if (entity.computer != null) {
                entity.computer.queueEvent("disk_unmounted", new Object[] {id});
            }
        }

        if (this.world.getBlockEntity(this.pos.add(0, 0, -1)) instanceof ComputerBlockEntity)  {
            entity = (ComputerBlockEntity) world.getBlockEntity(this.pos.add(0, 0, -1));

            if (entity.computer != null) {
                entity.computer.queueEvent("disk_unmounted", new Object[] {id});
            }
        }

        if (this.world.getBlockEntity(this.pos.add(0, 1, 0)) instanceof ComputerBlockEntity)  {
            entity = (ComputerBlockEntity) world.getBlockEntity(this.pos.add(0, 1, 0));

            if (entity.computer != null) {
                entity.computer.queueEvent("disk_unmounted", new Object[] {id});
            }
        }

        if (this.world.getBlockEntity(this.pos.add(0, -1, 0)) instanceof ComputerBlockEntity)  {

            entity = (ComputerBlockEntity) world.getBlockEntity(this.pos.add(0, -1, 0));

            if (entity.computer != null) {
                entity.computer.queueEvent("disk_unmounted", new Object[] {id});
            }
        }
    }

    public DiskDriveBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.DISK_DRIVE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new DiskDriveScreenHandler(syncId, playerInventory, this, this);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }
}
