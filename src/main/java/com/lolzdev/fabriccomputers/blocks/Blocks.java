package com.lolzdev.fabriccomputers.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {
    public static final Block COMPUTER_BLOCK = new ComputerBlock();
    public static final Block DISK_DRIVE_BLOCK = new DiskDriveBlock();

    public static void init() {
        Registry.register(Registry.BLOCK, new Identifier("fabriccomputers", "computer"), COMPUTER_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier("fabriccomputers", "disk_drive"), DISK_DRIVE_BLOCK);
    }
}
