package com.lolzdev.fabriccomputers.blockentities;

import com.lolzdev.fabriccomputers.api.IComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.carbon.vm2.LuaValue;
import org.carbon.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayDeque;
import java.util.Queue;

public class RedstoneComponentBlockEntity extends BlockEntity implements IComponent {
    public int output;
    public Queue<Integer> outputQueue;

    public RedstoneComponentBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.REDSTONE_COMPONENT_BLOCK_ENTITY, pos, state);

        this.output = 0;
        this.outputQueue = new ArrayDeque<>(5);
    }

    public void setOutput(int output) {
        this.outputQueue.offer(output);
    }

    public static void tick(RedstoneComponentBlockEntity blockEntity) {
        if (!blockEntity.outputQueue.isEmpty()) {
            blockEntity.output = blockEntity.outputQueue.poll();

            blockEntity.world.updateNeighbors(blockEntity.pos, blockEntity.world.getBlockState(blockEntity.pos).getBlock());
            blockEntity.world.updateNeighbor(blockEntity.pos, blockEntity.world.getBlockState(blockEntity.pos).getBlock(), blockEntity.pos);
            blockEntity.world.updateComparators(blockEntity.pos, blockEntity.world.getBlockState(blockEntity.pos).getBlock());
        }
    }

    @Override
    public String getComponentType() {
        return "redstone";
    }

    @Override
    public String getComponentUUID() {
        return null;
    }

    @Override
    public LuaValue getComponent() {
        return CoerceJavaToLua.coerce(this);
    }
}
