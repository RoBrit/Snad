package com.robrit.snad.blocks;

import com.robrit.snad.config.ConfigRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class SnadBlock extends FallingBlock {
    private final int dustColor;

    public SnadBlock(int dustColor, Properties properties) {
        super(properties);
        this.dustColor = dustColor;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getDustColor(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return this.dustColor;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        if (plantable.getPlantType(world, pos) == PlantType.DESERT) {
            return true;
        } else if (plantable.getPlantType(world, pos) == PlantType.BEACH) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                boolean isWater = world.getFluidState(pos.relative(direction)).is(FluidTags.WATER);
                boolean isFrostedIce = world.getBlockState(pos.relative(direction)).is(Blocks.FROSTED_ICE);
                if (!isWater && !isFrostedIce) {
                    continue;
                }

                return true;
            }
        }

        return false;
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
                    final BlockState nextBlockState = serverLevel.getBlockState(blockPos.above(height));

                    if (nextBlockState.is(blockAbove)) {
                        for (int growthAttempts = 0; growthAttempts < ConfigRegistry.GROWTH_SPEED.get(); growthAttempts++) {
                            nextBlockState.randomTick(serverLevel, blockPos.above(height), random);
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
