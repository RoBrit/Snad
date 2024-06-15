package com.robrit.snad;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;

/**
 * The Snad mod configuration.
 * We're using a custom config system here as from Forge 1.19+, The ForgeConfigSpec will no longer allow config values
 * to be used as part of the items registration process.
 */
public class SnadConfig {
    public static final SnadConfig INSTANCE = new SnadConfig();

    private final CommentedFileConfig config;

    public SnadConfig() {
        var configPath = FMLPaths.CONFIGDIR.get().resolve("snad-common.toml");

        ConfigSpec spec = new ConfigSpec();
        spec.defineInRange("Growth Speed", 2, 1, 20);
        spec.defineInRange("Movement Speed", 1.4, 1.0, 2.0);

        config = CommentedFileConfig.builder(configPath)
                .build();

        config.set("Growth Speed", 2);
        config.setComment("Growth Speed", "The multiplier for the chance of growth");
        config.set("Movement Speed", 1.4);
        config.setComment("Movement Speed", "The multiplier for movement speed on Suol Snad");

        // If the config file exists, load it
        if (Files.exists(configPath)) {
            config.load();
            spec.correct(config);
        } else {
            config.save();
        }

        config.close();
    }

    public int getGrowthSpeed() {
        return config.get("Growth Speed");
    }

    public double getMovementSpeed() {
        return config.get("Movement Speed");
    }
}
