package com.lolzdev.fabriccomputers.blocks;

import com.lolzdev.fabriccomputers.blockentities.ComputerBlockEntity;
import com.lolzdev.fabriccomputers.blockentities.ScreenComponentBlockEntity;
import com.lolzdev.fabriccomputers.common.packets.PixelBufferChangePacket;
import com.lolzdev.fabriccomputers.common.packets.ScreenSizePacket;
import com.lolzdev.fabriccomputers.computer.Computer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ScreenComponentBlock extends BlockWithEntity {

    protected ScreenComponentBlock() {
        super(FabricBlockSettings.of(Material.METAL));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return (NamedScreenHandlerFactory) world.getBlockEntity(pos);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (tickerWorld, pos, tickerState, blockEntity) -> {
            if (blockEntity instanceof ScreenComponentBlockEntity) {
                ScreenComponentBlockEntity.tick((ScreenComponentBlockEntity) blockEntity);
            }
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            ScreenComponentBlockEntity entity = (ScreenComponentBlockEntity) world.getBlockEntity(pos);

            if (entity != null && screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);

                ScreenSizePacket.send(player, entity);
                PixelBufferChangePacket.send(player, entity.getPixelBuffer(), 0, 0, entity.screenWidth - 1, entity.screenHeight - 1);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ScreenComponentBlockEntity(pos, state);
    }
}
