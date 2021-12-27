package com.lolzdev.fabriccomputers;

import com.lolzdev.fabriccomputers.blockentities.BlockEntities;
import com.lolzdev.fabriccomputers.blocks.Blocks;
import com.lolzdev.fabriccomputers.common.ComputerScreenHandler;
import com.lolzdev.fabriccomputers.common.DiskDriveScreenHandler;
import com.lolzdev.fabriccomputers.common.KeyPressedPacket;
import com.lolzdev.fabriccomputers.items.Items;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class FabricComputers implements ModInitializer {

    public static final ScreenHandlerType<ComputerScreenHandler> COMPUTER_SCREEN_HANDLER;
    public static final ScreenHandlerType<DiskDriveScreenHandler> DISK_DRIVE_SCREEN_HANDLER;

    static {
        COMPUTER_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier("fabriccomputers", "computer"), ComputerScreenHandler::new);
        DISK_DRIVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("fabriccomputers", "disk_drive"), DiskDriveScreenHandler::new);
    }

    @Override
    public void onInitialize() {
        Blocks.init();
        BlockEntities.init();
        Items.init();

        ServerPlayNetworking.registerGlobalReceiver(KeyPressedPacket.ID, KeyPressedPacket::handle);
    }
}
