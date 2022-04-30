package com.lolzdev.fabriccomputers.blockentities;

import com.lolzdev.fabriccomputers.blocks.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntities {
    public static BlockEntityType<ComputerBlockEntity> COMPUTER_BLOCK_ENTITY;
    //public static BlockEntityType<DiskDriveBlockEntity> DISK_DRIVE_BLOCK_ENTITY;
    public static BlockEntityType<RedstoneComponentBlockEntity> REDSTONE_COMPONENT_BLOCK_ENTITY;
    public static BlockEntityType<ScreenComponentBlockEntity> SCREEN_COMPONENT_BLOCK_ENTITY;

    public static void init() {
        COMPUTER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fabriccomputers", "computer"), FabricBlockEntityTypeBuilder.create(ComputerBlockEntity::new, Blocks.COMPUTER_BLOCK).build(null));
        //DISK_DRIVE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fabriccomptuers", "disk_drive"), FabricBlockEntityTypeBuilder.create(DiskDriveBlockEntity::new, Blocks.DISK_DRIVE_BLOCK).build(null));
        REDSTONE_COMPONENT_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fabriccomputers", "redstone_component"), FabricBlockEntityTypeBuilder.create(RedstoneComponentBlockEntity::new, Blocks.REDSTONE_COMPONENT_BLOCK).build(null));
        SCREEN_COMPONENT_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fabriccomputers", "screen_component"), FabricBlockEntityTypeBuilder.create(ScreenComponentBlockEntity::new, Blocks.SCREEN_COMPONENT_BLOCK).build(null));
    }
}
