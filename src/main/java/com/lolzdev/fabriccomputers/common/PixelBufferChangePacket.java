package com.lolzdev.fabriccomputers.common;

import com.lolzdev.fabriccomputers.client.screens.ComputerScreen;
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

    public static void send(PlayerEntity user, int startX, int endX, int startY, int endY, int[] pixels, boolean shouldUpdate) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(startX);
        data.writeInt(endX);
        data.writeInt(startY);
        data.writeInt(endY);
        data.writeIntArray(pixels);
        data.writeBoolean(shouldUpdate);

        ServerPlayNetworking.send((ServerPlayerEntity) user, ID, data);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
        int startX = buf.readInt();
        int endX = buf.readInt();
        int startY = buf.readInt();
        int endY = buf.readInt();
        int[] pixels = buf.readIntArray();
        boolean shouldUpdate = buf.readBoolean();

        client.execute(() -> {
            if (client.currentScreen instanceof ComputerScreen) {
                ((ComputerScreen) client.currentScreen).startX = startX;
                ((ComputerScreen) client.currentScreen).startY = startY;
                ((ComputerScreen) client.currentScreen).endX = endX;
                ((ComputerScreen) client.currentScreen).endY = endY;
                ((ComputerScreen) client.currentScreen).pixels = pixels;
                ((ComputerScreen) client.currentScreen).shouldUpdate = shouldUpdate;
            }
        });
    }
}
