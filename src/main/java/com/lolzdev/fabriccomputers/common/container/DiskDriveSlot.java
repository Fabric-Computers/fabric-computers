package com.lolzdev.fabriccomputers.common.container;

import com.lolzdev.fabriccomputers.blockentities.DiskDriveBlockEntity;
import com.lolzdev.fabriccomputers.items.FloppyDiskItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DiskDriveSlot extends Slot {

    public DiskDriveSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    protected void onTake(int amount) {
        super.onTake(amount);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof FloppyDiskItem && super.canInsert(stack);
    }
}
