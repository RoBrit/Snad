package com.robrit.snad.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.robrit.snad.Snad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.util.TriState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;

public class SnadBlock extends FallingBlock {
    public static final MapCodec<SnadBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.INT.fieldOf("dust_color").forGetter(e -> e.dustColor), propertiesCodec())
                    .apply(instance, SnadBlock::new)
    );

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
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        SuolSnadBlock.acceleratedTick(serverLevel, blockPos, random);
    }
}
