package com.lolzdev.fabriccomputers.client;

import com.lolzdev.fabriccomputers.FabricComputers;
import com.lolzdev.fabriccomputers.client.screens.ComputerScreen;
import com.lolzdev.fabriccomputers.client.screens.DiskDriveScreen;
import com.lolzdev.fabriccomputers.common.PixelBufferChangePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class FabricComputersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(FabricComputers.COMPUTER_SCREEN_HANDLER, ComputerScreen::new);
        ScreenRegistry.register(FabricComputers.DISK_DRIVE_SCREEN_HANDLER, DiskDriveScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(PixelBufferChangePacket.ID, PixelBufferChangePacket::handle);
    }
}
