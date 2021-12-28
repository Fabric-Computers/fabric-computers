package com.lolzdev.fabriccomputers.blockentities;

import com.lolzdev.fabriccomputers.api.IComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class RedstoneComponentBlockEntity extends BlockEntity implements IComponent {

    public int output;

    public RedstoneComponentBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.REDSTONE_COMPONENT_BLOCK_ENTITY, pos, state);

        this.output = 0;
    }

    public void setOutput(int output) {
        this.output = output;

        System.out.println("Output: " + this.output);

        world.updateNeighbors(this.pos, this.world.getBlockState(this.pos).getBlock());
        world.updateNeighbor(this.pos, this.world.getBlockState(this.pos).getBlock(), this.pos);

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
