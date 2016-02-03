/*
 * Snad.java
 *
 * Copyright (c) 2016 TheRoBrit
 *
 * SNAD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SNAD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robrit.snad.common;

import com.robrit.snad.common.event.ConfigurationHandler;
import com.robrit.snad.common.proxy.IProxy;
import com.robrit.snad.common.util.LogHelper;
import com.robrit.snad.common.util.ModInformation;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ModInformation.MOD_ID, name = ModInformation.MOD_NAME,
        version = ModInformation.MOD_VERSION, dependencies = ModInformation.MOD_DEPENDENCIES,
        certificateFingerprint = ModInformation.MOD_FINGERPRINT)
public class Snad {

    @Mod.Instance(ModInformation.MOD_ID)
    private static Snad instance;

    @SidedProxy(clientSide = ModInformation.PROXY_CLIENT_LOCATION,
            serverSide = ModInformation.PROXY_SERVER_LOCATION)
    private static IProxy proxy;

    public static Snad getInstance() {
        return instance;
    }

    public static IProxy getProxy() {
        return proxy;
    }

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.setConfigFile(event.getSuggestedConfigurationFile());
        ConfigurationHandler.init();
        ConfigurationHandler.updateConfiguration();

        proxy.registerBlocks();

        if (event.getSide() == Side.CLIENT) {
            proxy.registerBlockModels();
        }

        if (ModInformation.DEBUG_MODE) {
            LogHelper.info(String.format("Finished pre-initialisation stage for %s",
                    ModInformation.MOD_ID));
        }
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.registerRecipes();

        if (ModInformation.DEBUG_MODE) {
            LogHelper.info(String.format("Finished initialisation stage for %s",
                    ModInformation.MOD_ID));
        }
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        if (ModInformation.DEBUG_MODE) {
            LogHelper.info(String.format("Finished post-initialisation stage for %s",
                    ModInformation.MOD_ID));
        }
    }

    @Mod.EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent event) {
        if (ModInformation.MOD_FINGERPRINT.equals("@FINGERPRINT@")) {
            LogHelper.error("No fingerprint found!");
        } else {
            LogHelper.warn("Invalid fingerprint found!");
        }
    }
}
