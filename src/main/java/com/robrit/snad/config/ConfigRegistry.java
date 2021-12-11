package com.robrit.snad.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigRegistry {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.IntValue GROWTH_SPEED;
    public static final ForgeConfigSpec.DoubleValue MOVEMENT_SPEED;

    static {
        final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        GROWTH_SPEED = BUILDER
                .comment("The multiplier for the chance of growth")
                .defineInRange("Growth Speed", 2, 1, 20);

        MOVEMENT_SPEED = BUILDER
                .comment("The multiplier for movement speed on Suol Snad")
                .defineInRange("Movement Speed", 1.4, 1.0, 2.0);

        CONFIG = BUILDER.build();
    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG);
    }
}
