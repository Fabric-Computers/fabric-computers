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

public class ScreenSizePacket {
    public static final Identifier ID = new Identifier("fabriccomputers", "screen_size");

    public static void send(PlayerEntity player, Computer computer) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(computer.screenWidth);
        data.writeInt(computer.screenHeight);

        ServerPlayNetworking.send((ServerPlayerEntity) player, ID, data);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
        int width = buf.readInt();
        int height = buf.readInt();

        client.execute(() -> {
            if (client.currentScreen instanceof ComputerScreen screen) screen.initTexture(width, height);
        });
    }
}
