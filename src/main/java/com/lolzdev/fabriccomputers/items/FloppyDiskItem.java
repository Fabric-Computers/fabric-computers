package com.lolzdev.fabriccomputers.items;

import com.lolzdev.fabriccomputers.computer.FileSystem;
import com.lolzdev.fabriccomputers.computer.IFileSystem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class FloppyDiskItem extends Item {
    public IFileSystem fileSystem;

    public FloppyDiskItem() {
        super(new FabricItemSettings().group(ItemGroup.REDSTONE).maxCount(1));

        this.fileSystem = new FileSystem();
    }

    public ItemStack create(UUID id) {
        ItemStack result = new ItemStack(this);
        result.getOrCreateNbt().putString("uuid", id.toString());
        return result;
    }

    public static void mount(ItemStack stack) {
        if (stack.getItem() instanceof FloppyDiskItem) {
            if (stack.hasNbt()) {
                ((FloppyDiskItem) stack.getItem()).fileSystem.mount(stack.getNbt().getString("uuid"));
            } else {
                UUID id = UUID.randomUUID();
                stack.getOrCreateNbt().putString("uuid", id.toString());
                ((FloppyDiskItem) stack.getItem()).fileSystem.mount(id.toString());
            }
        }
    }

    @Override
    public void postProcessNbt(NbtCompound nbt) {
        super.postProcessNbt(nbt);
    }
}
