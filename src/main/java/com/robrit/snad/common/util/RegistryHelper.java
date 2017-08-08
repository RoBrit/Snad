package com.robrit.snad.common.util;

import com.robrit.snad.common.Snad;
import com.robrit.snad.common.item.ItemBlockSnadMeta;
import com.robrit.snad.common.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHelper {

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    event.getRegistry().register(CommonProxy.blockSnad);
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    event.getRegistry().register(new ItemBlockSnadMeta(CommonProxy.blockSnad).setRegistryName(CommonProxy.blockSnad.getRegistryName()));
  }

}
