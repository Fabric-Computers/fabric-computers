package com.lolzdev.fabriccomputers.client.screens;

import com.lolzdev.fabriccomputers.common.packets.CharTypedPacket;
import com.lolzdev.fabriccomputers.common.packets.KeyPressedPacket;
import com.lolzdev.fabriccomputers.common.packets.KeyUpPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.glfw.GLFW;

public class ScreenComponentScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("fabriccomputers", "textures/gui/computer.png");

    private NativeImageBackedTexture texture;

    public ScreenComponentScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundWidth = 256;
        backgroundHeight = 187;
    }

    public void initTexture(int screenWidth, int screenHeight) {
        if (texture != null) throw new IllegalStateException("initTexture() called twice one the same ComputerScreen instance");

        texture = new NativeImageBackedTexture(screenWidth, screenHeight, false);
    }

    @Override
    public void onClose() {
        texture.close();

        super.onClose();
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
    }

    @Override
    public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        // Draw outline
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Draw screen
        if (texture != null) {
            RenderSystem.setShaderTexture(0, texture.getGlId());
            drawTexturedQuad(matrices, x + 6, y + 6, 245, 177);
        }
    }

    private void drawTexturedQuad(MatrixStack matrices, int x, int y, int width, int height) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, x, y + height, 0).texture(0, 1).next();
        bufferBuilder.vertex(matrix, x + width, y + height, 0).texture(1, 1).next();
        bufferBuilder.vertex(matrix, x + width, y, 0).texture(1, 0).next();
        bufferBuilder.vertex(matrix, x, y, 0).texture(0, 0).next();
        bufferBuilder.end();

        BufferRenderer.draw(bufferBuilder);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void updateScreen(int[] pixels, int startX, int startY, int endX, int endY) {
        NativeImage image = texture.getImage();

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                image.setColor(x, y, pixels[x * image.getHeight() + y]);
            }
        }

        texture.upload();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        } else {
            KeyPressedPacket.send(keyCode);
            return true;
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            return super.keyReleased(keyCode, scanCode, modifiers);
        } else {
            KeyUpPacket.send(keyCode);
            return true;
        }
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {

        CharTypedPacket.send(chr);

        return super.charTyped(chr, modifiers);
    }
}
