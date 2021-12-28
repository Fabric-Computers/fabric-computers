package com.lolzdev.fabriccomputers.common.packets;

import com.lolzdev.fabriccomputers.common.ComputerScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class KeyPressedPacket {
    public static final Identifier ID = new Identifier("fabriccomputers", "key_pressed");

    public static void send(int keyCode) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(keyCode);

        ClientPlayNetworking.send(ID, data);
    }

    public static void handle(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {
        int keyCode = buf.readInt();

        minecraftServer.execute(() -> {
            if (serverPlayerEntity.currentScreenHandler instanceof ComputerScreenHandler) {
                ((ComputerScreenHandler) serverPlayerEntity.currentScreenHandler).keyDown(keyCode);
            }
        });
    }
}
