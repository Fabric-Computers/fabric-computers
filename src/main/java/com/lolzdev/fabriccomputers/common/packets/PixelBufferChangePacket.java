package com.lolzdev.fabriccomputers.common.packets;

import com.lolzdev.fabriccomputers.client.screens.ComputerScreen;
import com.lolzdev.fabriccomputers.computer.Computer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PixelBufferChangePacket {
    public static final Identifier ID = new Identifier("fabriccomputers", "pixel_buffer_change");

    public static void send(PlayerEntity user, int[] pixels, int startX, int startY, int endX, int endY) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeIntArray(pixels);
        data.writeInt(startX);
        data.writeInt(startY);
        data.writeInt(endX);
        data.writeInt(endY);

        ServerPlayNetworking.send((ServerPlayerEntity) user, ID, data);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
        int[] pixels = buf.readIntArray();
        int startX = buf.readInt();
        int startY = buf.readInt();
        int endX = buf.readInt();
        int endY = buf.readInt();

        client.execute(() -> {
            if (client.currentScreen instanceof ComputerScreen screen) screen.updateScreen(pixels, startX, startY, endX, endY);
        });
    }
}
