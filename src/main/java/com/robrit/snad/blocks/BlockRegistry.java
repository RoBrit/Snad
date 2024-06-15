package com.robrit.snad.blocks;

import com.robrit.snad.References;
import com.robrit.snad.Snad;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Snad.MOD_ID);

    public static final DeferredHolder<Block, SnadBlock> SNAD = BLOCKS.register(References.ID_BLOCK_SNAD, () -> new SnadBlock(14406560, BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).randomTicks()));
    public static final DeferredHolder<Block, SnadBlock> RED_SNAD = BLOCKS.register(References.ID_BLOCK_RED_SNAD, () -> new SnadBlock(14406560, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND).randomTicks()));
    public static final DeferredHolder<Block, SuolSnadBlock> SUOL_SNAD = BLOCKS.register(References.ID_BLOCK_SUOL_SNAD, () -> new SuolSnadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND).randomTicks().speedFactor((float) Snad.config().getMovementSpeed())));

    public static void init(IEventBus eventBus) {
        BlockRegistry.BLOCKS.register(eventBus);
    }
}
