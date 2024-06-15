package com.robrit.snad.blocks;

import com.robrit.snad.References;
import com.robrit.snad.Snad;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Snad.MOD_ID);

    public static final RegistryObject<Block> SNAD = BLOCKS.register(References.ID_BLOCK_SNAD, () -> new SnadBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND).randomTicks()));
    public static final RegistryObject<Block> RED_SNAD = BLOCKS.register(References.ID_BLOCK_RED_SNAD, () -> new SnadBlock(14406560, BlockBehaviour.Properties.copy(Blocks.RED_SAND).randomTicks()));
    public static final RegistryObject<Block> SUOL_SNAD = BLOCKS.register(References.ID_BLOCK_SUOL_SNAD, () -> new SuolSnadBlock(BlockBehaviour.Properties.copy(Blocks.SOUL_SAND).randomTicks().speedFactor((float) Snad.config().getMovementSpeed())));

    public static void init() {
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
