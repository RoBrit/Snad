package com.robrit.snad.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.ParametersAreNonnullByDefault;

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
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        this.tick(blockState, serverLevel, blockPos, random);
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        SuolSnadBlock.acceleratedTick(serverLevel, blockPos, random);
    }
}
