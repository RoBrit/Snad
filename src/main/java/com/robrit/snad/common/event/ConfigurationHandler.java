/*
 * ConfigurationHandler.java
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

package com.robrit.snad.common.event;

import com.robrit.snad.common.util.ConfigurationData;
import com.robrit.snad.common.util.LogHelper;
import com.robrit.snad.common.util.ModInformation;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler {

  private static Configuration configuration;
  private static File configFile;

  public static void init() {
    setConfiguration(new Configuration(configFile));
  }

  public static void updateConfiguration() {
    try {
      configuration.load();

      ConfigurationData.SPEED_INCREASE_VALUE =
          configuration.get(ConfigurationData.GROUP_GENERAL_KEY,
                            ConfigurationData.SPEED_INCREASE_KEY,
                            ConfigurationData.SPEED_INCREASE_DEFAULT_VALUE).getInt();


    } catch (Exception exception) {
      LogHelper.error("Unable to read configuration for " + ModInformation.MOD_NAME);
      LogHelper.error(exception);
    } finally {
      if (configuration.hasChanged()) {
        configuration.save();
      }
    }
  }

  public static Configuration getConfiguration() {
    return configuration;
  }

  public static void setConfiguration(Configuration newConfiguration) {
    configuration = newConfiguration;
  }

  public static File getConfigFile() {
    return configFile;
  }

  public static void setConfigFile(File newConfigFile) {
    configFile = newConfigFile;
  }

  @SubscribeEvent
  public static void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.modID.equalsIgnoreCase(ModInformation.MOD_ID)) {
      updateConfiguration();
    }
  }
}
