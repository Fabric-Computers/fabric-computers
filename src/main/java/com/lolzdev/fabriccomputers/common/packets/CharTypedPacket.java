package com.lolzdev.fabriccomputers.common.packets;

import com.lolzdev.fabriccomputers.common.ScreenComponentScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CharTypedPacket {
    public static final Identifier ID = new Identifier("fabriccomputers", "char_typed");

    public static void send(char charcode) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeChar(charcode);

        ClientPlayNetworking.send(ID, data);
    }

    public static void handle(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {
        char charcode = buf.readChar();

        minecraftServer.execute(() -> {
            if (serverPlayerEntity.currentScreenHandler instanceof ScreenComponentScreenHandler) {
                ((ScreenComponentScreenHandler) serverPlayerEntity.currentScreenHandler).charTyped(charcode);
            }
        });
    }
}
