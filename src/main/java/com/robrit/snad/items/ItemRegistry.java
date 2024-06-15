package com.robrit.snad.items;

import com.robrit.snad.References;
import com.robrit.snad.Snad;
import com.robrit.snad.blocks.BlockRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Snad.MOD_ID);

    public static final RegistryObject<Item> SNAD = ITEMS.register(References.ID_BLOCK_SNAD, () -> new BlockItem(BlockRegistry.SNAD.get(), new Item.Properties()));
    public static final RegistryObject<Item> RED_SNAD = ITEMS.register(References.ID_BLOCK_RED_SNAD, () -> new BlockItem(BlockRegistry.RED_SNAD.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUOL_SNAD = ITEMS.register(References.ID_BLOCK_SUOL_SNAD, () -> new BlockItem(BlockRegistry.SUOL_SNAD.get(), new Item.Properties()));

    public static void init() {
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
