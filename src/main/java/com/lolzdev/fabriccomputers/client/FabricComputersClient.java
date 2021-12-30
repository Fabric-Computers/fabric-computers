package com.lolzdev.fabriccomputers.client;

import com.lolzdev.fabriccomputers.FabricComputers;
import com.lolzdev.fabriccomputers.blocks.Blocks;
import com.lolzdev.fabriccomputers.client.screens.DiskDriveScreen;
import com.lolzdev.fabriccomputers.client.screens.ScreenComponentScreen;
import com.lolzdev.fabriccomputers.common.packets.PixelBufferChangePacket;
import com.lolzdev.fabriccomputers.common.packets.ScreenSizePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class FabricComputersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(FabricComputers.DISK_DRIVE_SCREEN_HANDLER, DiskDriveScreen::new);
        ScreenRegistry.register(FabricComputers.SCREEN_COMPONENT_SCREEN_HANDLER, ScreenComponentScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(ScreenSizePacket.ID, ScreenSizePacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(PixelBufferChangePacket.ID, PixelBufferChangePacket::handle);

        Blocks.clientInit();
    }
}
