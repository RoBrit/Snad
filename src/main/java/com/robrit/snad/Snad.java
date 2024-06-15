package com.robrit.snad;

import com.robrit.snad.blocks.BlockRegistry;
import com.robrit.snad.items.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("snad")
public class Snad {
    public static final String MOD_ID = "snad";

    public static final TagKey<Block> SNAD_BLOCKS = TagKey.create(Registries.BLOCK, snadId("snad"));

    public Snad() {
        BlockRegistry.init();
        ItemRegistry.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(Snad::registerCreativeTab);
    }

    @SubscribeEvent
    public static void registerCreativeTab(BuildCreativeModeTabContentsEvent event) {
        var tab = event.getTabKey();
        if (tab != CreativeModeTabs.BUILDING_BLOCKS) {
            return;
        }

        event.accept(BlockRegistry.SNAD);
        event.accept(BlockRegistry.RED_SNAD);
        event.accept(BlockRegistry.SUOL_SNAD);
    }

    public static SnadConfig config() {
        return SnadConfig.INSTANCE;
    }

    public static ResourceLocation snadId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
