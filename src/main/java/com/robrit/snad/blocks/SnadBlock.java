package com.robrit.snad.blocks;

import com.robrit.snad.Snad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.util.TriState;

import javax.annotation.ParametersAreNonnullByDefault;

public class SnadBlock extends ColoredFallingBlock {
    public SnadBlock(int dustColor, Properties properties) {
        super(new ColorRGBA(dustColor), properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPos, Direction facing, BlockState plant) {
        if (plant.is(Snad.SNAD_PLACEABLE_CROPS)) {
            if (plant.is(Snad.SNAD_REQUIRES_WATER)) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockState blockstate1 = level.getBlockState(soilPos.relative(direction));
                    FluidState fluidstate = level.getFluidState(soilPos.relative(direction));
                    if (state.canBeHydrated(level, soilPos.above(), fluidstate, soilPos.relative(direction)) || blockstate1.is(Blocks.FROSTED_ICE)) {
                        return TriState.TRUE;
                    }
                }

                return TriState.FALSE;
            }

            return TriState.TRUE;
        }

        return TriState.FALSE;
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        this.tick(blockState, serverLevel, blockPos, random);
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        super.tick(blockState, serverLevel, blockPos, random);
        SuolSnadBlock.acceleratedTick(serverLevel, blockPos, random);
    }
}
