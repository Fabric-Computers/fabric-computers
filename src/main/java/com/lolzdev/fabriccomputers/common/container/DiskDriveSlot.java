package com.lolzdev.fabriccomputers.common.container;

import com.lolzdev.fabriccomputers.blockentities.DiskDriveBlockEntity;
import com.lolzdev.fabriccomputers.items.FloppyDiskItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DiskDriveSlot extends Slot {

    DiskDriveBlockEntity entity;
    FloppyDiskItem current;



    public DiskDriveSlot(Inventory inventory, int index, int x, int y, DiskDriveBlockEntity entity) {
        super(inventory, index, x, y);

        this.entity = entity;
    }

    @Override
    public ItemStack takeStack(int amount) {
        if (this.entity != null) {
            if (this.current != null) {
                //this.entity.diskRemoved(
                //        this.current.fileSystem.getUUIDOrRandom()
                //);
                this.current = null;
            }
            System.out.println("Taken");
        }

        return super.takeStack(amount);
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        this.current = (FloppyDiskItem) stack.getItem();

        System.out.println("Inserted");

        return super.insertStack(stack, count);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof FloppyDiskItem && super.canInsert(stack);
    }
}
