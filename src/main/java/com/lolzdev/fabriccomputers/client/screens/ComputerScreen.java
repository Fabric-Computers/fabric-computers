package com.lolzdev.fabriccomputers.client.screens;

import com.lolzdev.fabriccomputers.common.KeyPressedPacket;
import com.lolzdev.fabriccomputers.common.KeyUpPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ComputerScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("fabriccomputers", "textures/gui/computer.png");
    private static final Identifier PIXEL = new Identifier("fabriccomputers", "textures/gui/pixel.png");
    public int startX = 0, endX = 0, startY = 0, endY = 0;
    public int[] pixels;
    public boolean shouldUpdate;
    public NativeImageBackedTexture texture;

    public ComputerScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
    }

    @Override
    public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        backgroundWidth = 256;
        backgroundHeight = 187;
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        NativeImage image = new NativeImage(245, 178, false);

        int terminalWidth = 243, terminalHeight = 176;

        int cur = 0;

        if (this.pixels != null) {

            for (int xPos = 0; xPos < 245; xPos++) {
                for (int yPos = 0; yPos < 177; yPos++) {
                    int p = pixels[cur];
                    image.setColor(xPos, yPos, p);

                    cur++;

                }
            }

            this.texture = new NativeImageBackedTexture(image);
            image.close();
            RenderSystem.setShaderTexture(0, texture.getGlId());

            x += 6;
            y += 6;

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            drawTexture(matrices, x, y, 0, 0, 244, 177);

        }




    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

    }

    @Override
    protected void init() {
        super.init();

        this.endX = 245;
        this.endY = 177;
        this.shouldUpdate = true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        KeyPressedPacket.send(keyCode);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {

        KeyUpPacket.send(keyCode);

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
