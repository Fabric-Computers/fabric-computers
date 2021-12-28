package com.lolzdev.fabriccomputers;

import com.lolzdev.fabriccomputers.blockentities.BlockEntities;
import com.lolzdev.fabriccomputers.blocks.Blocks;
import com.lolzdev.fabriccomputers.common.DiskDriveScreenHandler;
import com.lolzdev.fabriccomputers.common.ScreenComponentScreenHandler;
import com.lolzdev.fabriccomputers.common.packets.KeyPressedPacket;
import com.lolzdev.fabriccomputers.common.packets.KeyUpPacket;
import com.lolzdev.fabriccomputers.items.Items;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class FabricComputers implements ModInitializer {

    public static final ScreenHandlerType<ScreenComponentScreenHandler> SCREEN_COMPONENT_SCREEN_HANDLER;
    public static final ScreenHandlerType<DiskDriveScreenHandler> DISK_DRIVE_SCREEN_HANDLER;

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("fabriccomputers", "main"), () -> new ItemStack(Blocks.COMPUTER_BLOCK));

    static {
        SCREEN_COMPONENT_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier("fabriccomputers", "screen_component"), ScreenComponentScreenHandler::new);
        DISK_DRIVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("fabriccomputers", "disk_drive"), DiskDriveScreenHandler::new);
    }

    @Override
    public void onInitialize() {
        Blocks.init();
        BlockEntities.init();
        Items.init();

        ServerPlayNetworking.registerGlobalReceiver(KeyPressedPacket.ID, KeyPressedPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(KeyUpPacket.ID, KeyUpPacket::handle);
    }
}
