package com.robrit.snad;

import com.robrit.snad.blocks.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Snad.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SnadData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();

        if (event.includeClient()) {
            generator.addProvider(true, new Lang(packOutput));
            generator.addProvider(true, new BlockStates(packOutput, existingFileHelper));
            generator.addProvider(true, new ItemModels(packOutput, existingFileHelper));
        }

        if (event.includeServer()) {
            generator.addProvider(true, new Recipes(packOutput, event.getLookupProvider()));
            generator.addProvider(true, new TagGenerator(packOutput, event.getLookupProvider(), existingFileHelper));
        }
    }

    public static final class ItemModels extends ItemModelProvider {
        public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, Snad.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            withExistingParent("snad", modLoc("block/snad"));
            withExistingParent("red_snad", modLoc("block/red_snad"));
            withExistingParent("suol_snad", modLoc("block/suol_snad"));
        }
    }

    public static final class BlockStates extends BlockStateProvider {
        public BlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
            super(output, Snad.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(BlockRegistry.SNAD.get());
            simpleBlock(BlockRegistry.RED_SNAD.get());
            simpleBlock(BlockRegistry.SUOL_SNAD.get());
        }
    }

    public static final class TagGenerator extends BlockTagsProvider {
        public TagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Snad.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            this.tag(Snad.SNAD_BLOCKS).add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get(), BlockRegistry.SUOL_SNAD.get());

            this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("cactus_plantable_on")))
                    .add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get());

            this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("nether_wart_plantable_on")))
                    .add(BlockRegistry.SUOL_SNAD.get());

            this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("sugar_cane_plantable_on")))
                    .add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get());

            this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("bamboo_plantable_on")))
                    .add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get());
        }
    }

    public static final class Recipes extends RecipeProvider {
        public Recipes(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
            super(pOutput, pRegistries);
        }

        @Override
        protected void buildRecipes(RecipeOutput consumer) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.RED_SNAD.get())
                    .requires(Items.RED_SAND)
                    .requires(Items.RED_SAND)
                    .unlockedBy("has_red_sand", has(Items.RED_SAND))
                    .save(consumer);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SNAD.get())
                    .requires(Items.SAND)
                    .requires(Items.SAND)
                    .unlockedBy("has_sand", has(Items.SAND))
                    .save(consumer);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SUOL_SNAD.get())
                    .requires(Items.SOUL_SAND)
                    .requires(Items.SOUL_SAND)
                    .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                    .save(consumer);
        }
    }

    public static final class Lang extends LanguageProvider {
        public Lang(PackOutput output) {
            super(output, Snad.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            this.add("block.snad.snad", "Snad");
            this.add("block.snad.red_snad", "Red Snad");
            this.add("block.snad.suol_snad", "Suol Snad");
        }
    }
}
