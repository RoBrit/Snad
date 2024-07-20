package com.robrit.snad.blocks;

import com.robrit.snad.Snad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriState;

import javax.annotation.ParametersAreNonnullByDefault;

public class SuolSnadBlock extends Block {
    public SuolSnadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
        if (plant.is(Snad.SUOL_PLACEABLE_CROPS)) {
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
        acceleratedTick(serverLevel, blockPos, random);
    }

    public static void acceleratedTick(ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        final BlockState blockAbove = serverLevel.getBlockState(blockPos.above());

        if (!blockAbove.is(Snad.SNAD_PLACEABLE_CROPS) && !blockAbove.is(Snad.SUOL_PLACEABLE_CROPS)) {
            return;
        }

        boolean isSameBlockType = true;
        int height = 1;

        while (isSameBlockType) {
            if (blockPos.above(height).getY() < serverLevel.getMaxBuildHeight()) {
                final BlockState nextBlock = serverLevel.getBlockState(blockPos.above(height));

                if (nextBlock.is(blockAbove.getBlock())) {
                    for (int growthAttempts = 0; growthAttempts < Snad.config().getGrowthSpeed(); growthAttempts++) {
                        nextBlock.randomTick(serverLevel, blockPos.above(height), random);
                    }

                    height++;
                } else {
                    isSameBlockType = false;
                }
            } else {
                isSameBlockType = false;
            }
        }
    }
}
