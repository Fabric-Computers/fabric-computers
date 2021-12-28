package com.lolzdev.fabriccomputers.blocks;

import com.lolzdev.fabriccomputers.blockentities.ComputerBlockEntity;
import com.lolzdev.fabriccomputers.common.packets.PixelBufferChangePacket;
import com.lolzdev.fabriccomputers.common.packets.ScreenSizePacket;
import com.lolzdev.fabriccomputers.computer.Computer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class ComputerBlock extends BlockWithEntity {

    public ComputerBlock() {
        super(FabricBlockSettings.of(Material.METAL));
        setDefaultState( getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(Properties.POWERED, false) );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, Properties.POWERED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (tickerWorld, pos, tickerState, blockEntity) -> {
            if (blockEntity instanceof ComputerBlockEntity) {
                ComputerBlockEntity.tick((ComputerBlockEntity) blockEntity);
            }
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            ComputerBlockEntity entity = (ComputerBlockEntity) world.getBlockEntity(pos);

            if (entity != null && entity.computer != null && screenHandlerFactory != null) {
                Computer computer = entity.computer;
                player.openHandledScreen(screenHandlerFactory);

                ScreenSizePacket.send(player, computer);
                PixelBufferChangePacket.send(player, computer.getPixelBuffer(), 0, 0, computer.screenWidth - 1, computer.screenHeight - 1);

                if (computer.halted) computer.boot();
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return (NamedScreenHandlerFactory) world.getBlockEntity(pos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if( ctx.getPlayer() != null && state != null ) {
            return state.with(Properties.HORIZONTAL_FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
        }else{
            return getDefaultState();
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (placer instanceof PlayerEntity) {
            if (!world.isClient()) {
                ComputerBlockEntity entity = (ComputerBlockEntity) world.getBlockEntity(pos);
                if (entity != null && entity.computer != null) {
                    entity.computer.setId(UUID.randomUUID());
                }
            }
        }
    }
}
