package com.robrit.snad.blocks;

import com.robrit.snad.Snad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.common.PlantType;

import javax.annotation.ParametersAreNonnullByDefault;

public class SuolSnadBlock extends Block {
    public SuolSnadBlock(Properties properties) {
        super(properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return plantable.getPlantType(world, pos) == PlantType.NETHER;
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

        if (blockAbove.getBlock() instanceof IPlantable || blockAbove.is(Blocks.BAMBOO_SAPLING) || blockAbove.is(Blocks.BAMBOO)) {
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
}
