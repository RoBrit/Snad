package com.robrit.snad;

import com.robrit.snad.blocks.BlockRegistry;
import com.robrit.snad.config.ConfigRegistry;
import com.robrit.snad.items.ItemRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod("snad")
public class Snad {
    public static final String MOD_ID = "snad";

    public Snad() {
        ConfigRegistry.init();
        BlockRegistry.init();
        ItemRegistry.init();
    }
}
