package com.robrit.snad.items;

import com.robrit.snad.References;
import com.robrit.snad.Snad;
import com.robrit.snad.blocks.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Snad.MOD_ID);

    public static final DeferredHolder<Item, BlockItem> SNAD = ITEMS.register(References.ID_BLOCK_SNAD, () -> new BlockItem(BlockRegistry.SNAD.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Snad.snadId(References.ID_BLOCK_SNAD))).useBlockDescriptionPrefix()));
    public static final DeferredHolder<Item, BlockItem> RED_SNAD = ITEMS.register(References.ID_BLOCK_RED_SNAD, () -> new BlockItem(BlockRegistry.RED_SNAD.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Snad.snadId(References.ID_BLOCK_RED_SNAD))).useBlockDescriptionPrefix()));
    public static final DeferredHolder<Item, BlockItem> SUOL_SNAD = ITEMS.register(References.ID_BLOCK_SUOL_SNAD, () -> new BlockItem(BlockRegistry.SUOL_SNAD.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Snad.snadId(References.ID_BLOCK_SUOL_SNAD))).useBlockDescriptionPrefix()));

    public static void init(IEventBus eventBus) {
        ItemRegistry.ITEMS.register(eventBus);
    }
}
