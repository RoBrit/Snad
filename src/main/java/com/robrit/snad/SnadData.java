package com.robrit.snad;

import com.robrit.snad.blocks.BlockRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Snad.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SnadData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new Lang(packOutput));
        generator.addProvider(true, new BlockStates(packOutput));
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new Recipes.Runner(packOutput, lookupProvider));
        generator.addProvider(true, new TagGenerator(packOutput, lookupProvider));
        generator.addProvider(true, new LootTableProvider(packOutput, Set.of(), List.of(new LootTableProvider.SubProviderEntry(Loot::new, LootContextParamSets.BLOCK)), lookupProvider));
    }

    public static final class BlockStates extends ModelProvider {
        public BlockStates(PackOutput output) {
            super(output, Snad.MOD_ID);
        }

        @Override
        protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
            blockModels.createTrivialBlock(BlockRegistry.SNAD.get(), TexturedModel.CUBE);
            blockModels.createTrivialBlock(BlockRegistry.RED_SNAD.get(), TexturedModel.CUBE);
            blockModels.createTrivialBlock(BlockRegistry.SUOL_SNAD.get(), TexturedModel.CUBE);

            // TODO: fix me
//            itemModels.create
//
//            withExistingParent("snad", modLoc("block/snad"));
//            withExistingParent("red_snad", modLoc("block/red_snad"));
//            withExistingParent("suol_snad", modLoc("block/suol_snad"));
        }
    }

    public static final class TagGenerator extends BlockTagsProvider {
        public TagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, Snad.MOD_ID);
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

            this.tag(Snad.SNAD_PLACEABLE_CROPS)
                    .add(Blocks.BAMBOO)
                    .add(Blocks.BAMBOO_SAPLING)
                    .add(Blocks.CACTUS)
                    .add(Blocks.SUGAR_CANE);

            this.tag(Snad.SNAD_REQUIRES_WATER)
                    .add(Blocks.SUGAR_CANE);

            this.tag(Snad.SUOL_PLACEABLE_CROPS)
                    .add(Blocks.NETHER_WART);
        }
    }

    public static final class Recipes extends RecipeProvider {
        private Recipes(HolderLookup.Provider p_360573_, RecipeOutput p_360872_) {
            super(p_360573_, p_360872_);
        }

        @Override
        protected void buildRecipes() {
            this.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.RED_SNAD.get())
                    .requires(Items.RED_SAND)
                    .requires(Items.RED_SAND)
                    .unlockedBy("has_red_sand", has(Items.RED_SAND))
                    .save(this.output);

            this.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SNAD.get())
                    .requires(Items.SAND)
                    .requires(Items.SAND)
                    .unlockedBy("has_sand", has(Items.SAND))
                    .save(this.output);

            this.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SUOL_SNAD.get())
                    .requires(Items.SOUL_SAND)
                    .requires(Items.SOUL_SAND)
                    .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                    .save(this.output);
        }

        public static class Runner extends RecipeProvider.Runner {
            public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
                super(output, registries);
            }

            @Override
            protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
                return new Recipes(registries, output);
            }

            @Override
            public String getName() {
                return "Snad Recipes";
            }
        }
    }

    public static final class Lang extends LanguageProvider {
        public Lang(PackOutput output) {
            super(output, Snad.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            this.addBlock(BlockRegistry.SNAD, "Snad");
            this.addBlock(BlockRegistry.RED_SNAD, "Red Snad");
            this.addBlock(BlockRegistry.SUOL_SNAD, "Suol Snad");

        }
    }

    public static final class Loot extends BlockLootSubProvider {
        private Loot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            this.dropSelf(BlockRegistry.SNAD.get());
            this.dropSelf(BlockRegistry.RED_SNAD.get());
            this.dropSelf(BlockRegistry.SUOL_SNAD.get());
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return List.of(
                    BlockRegistry.SNAD.get(),
                    BlockRegistry.RED_SNAD.get(),
                    BlockRegistry.SUOL_SNAD.get()
            );
        }
    }
}
