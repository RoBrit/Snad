package com.robrit.snad.blocks;

import com.robrit.snad.config.ConfigRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

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
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        this.tick(blockState, serverLevel, blockPos, random);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        final Block blockAbove = serverLevel.getBlockState(blockPos.above()).getBlock();

        if (blockAbove instanceof IPlantable) {
            boolean isSameBlockType = true;
            int height = 1;

            while (isSameBlockType) {
                if (blockPos.above(height).getY() < serverLevel.getMaxBuildHeight()) {
                    final Block nextBlock = serverLevel.getBlockState(blockPos.above(height)).getBlock();

                    if (nextBlock.getClass() == blockAbove.getClass()) {
                        for (int growthAttempts = 0; growthAttempts < ConfigRegistry.GROWTH_SPEED.get(); growthAttempts++) {
                            nextBlock.randomTick(serverLevel.getBlockState(blockPos.above(height)), serverLevel, blockPos.above(height), random);
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
