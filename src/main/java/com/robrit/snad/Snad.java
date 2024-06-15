package com.robrit.snad;

import com.robrit.snad.blocks.BlockRegistry;
import com.robrit.snad.items.ItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;

@Mod("snad")
public class Snad {
    public static final String MOD_ID = "snad";

    public static final TagKey<Block> SNAD_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, snadId("snad"));

    public Snad() {
        BlockRegistry.init();
        ItemRegistry.init();
    }

    public static SnadConfig config() {
        return SnadConfig.INSTANCE;
    }

    public static ResourceLocation snadId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
