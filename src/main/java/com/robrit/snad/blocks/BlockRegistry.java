package com.robrit.snad.blocks;

import com.robrit.snad.config.ConfigRegistry;
import com.robrit.snad.References;
import com.robrit.snad.Snad;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Snad.MOD_ID);

    public static final RegistryObject<Block> SNAD = BLOCKS.register(References.ID_BLOCK_SNAD, () -> new SnadBlock(14406560, BlockBehaviour.Properties.of(Material.SAND, MaterialColor.SAND).randomTicks().strength(0.5F).sound(SoundType.SAND)));
    public static final RegistryObject<Block> RED_SNAD = BLOCKS.register(References.ID_BLOCK_RED_SNAD, () -> new SnadBlock(14406560, BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_ORANGE).randomTicks().strength(0.5F).sound(SoundType.SAND)));
    public static final RegistryObject<Block> SUOL_SNAD = BLOCKS.register(References.ID_BLOCK_SUOL_SNAD, () -> new SuolSnadBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_BROWN).randomTicks().strength(0.5F).speedFactor(ConfigRegistry.MOVEMENT_SPEED.get().floatValue()).sound(SoundType.SOUL_SAND)));


    public static void init() {
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
