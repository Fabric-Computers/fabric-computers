package com.lolzdev.fabriccomputers.items;

import com.lolzdev.fabriccomputers.FabricComputers;
import com.lolzdev.fabriccomputers.blocks.Blocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    public static void init() {
        Registry.register(Registry.ITEM, new Identifier("fabriccomputers", "computer"), new BlockItem(Blocks.COMPUTER_BLOCK, new FabricItemSettings().group(FabricComputers.ITEM_GROUP)));
        Registry.register(Registry.ITEM, new Identifier("fabriccomputers", "disk_drive"), new BlockItem(Blocks.DISK_DRIVE_BLOCK, new FabricItemSettings().group(FabricComputers.ITEM_GROUP)));
        Registry.register(Registry.ITEM, new Identifier("fabriccomputers", "redstone_component"), new BlockItem(Blocks.REDSTONE_COMPONENT_BLOCK, new FabricItemSettings().group(FabricComputers.ITEM_GROUP)));
        Registry.register(Registry.ITEM, new Identifier("fabriccomputers", "screen_component"), new BlockItem(Blocks.SCREEN_COMPONENT_BLOCK, new FabricItemSettings().group(FabricComputers.ITEM_GROUP)));

        Registry.register(Registry.ITEM, new Identifier("fabriccomputers", "floppy_disk"), new FloppyDiskItem());
        Registry.register(Registry.ITEM, new Identifier("fabriccomputers", "fos_disk"), new FixedFloppyDiskItem("fos"));
        Registry.register(Registry.ITEM, new Identifier("fabriccomputers", "snake_disk"), new FixedFloppyDiskItem("snake"));
    }
}
