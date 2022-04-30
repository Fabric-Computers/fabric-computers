package com.lolzdev.fabriccomputers.items;

import com.lolzdev.fabriccomputers.FabricComputers;
import com.lolzdev.fabriccomputers.computer.Drive;
import com.lolzdev.fabriccomputers.computer.FileSystem;
import com.lolzdev.fabriccomputers.api.IFileSystem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.BitSet;
import java.util.UUID;

public class FloppyDiskItem extends Item {
    Drive drive;
    public FloppyDiskItem(int size) {
        super(new FabricItemSettings().group(FabricComputers.ITEM_GROUP).maxCount(1));
        this.drive = new Drive(size, UUID.randomUUID());
    }

    public ItemStack create(UUID id) {
        ItemStack result = new ItemStack(this);
        result.getOrCreateNbt().putString("uuid", id.toString());
        return result;
    }

    @Override
    public void postProcessNbt(NbtCompound nbt) {
        super.postProcessNbt(nbt);
    }
}
