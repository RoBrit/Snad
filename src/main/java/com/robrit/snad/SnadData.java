package com.robrit.snad;

import com.robrit.snad.blocks.BlockRegistry;
import com.robrit.snad.items.ItemRegistry;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Snad.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SnadData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            generator.addProvider(true, new Lang(generator));
            generator.addProvider(true, new BlockStates(generator, existingFileHelper));
            generator.addProvider(true, new ItemModels(generator, existingFileHelper));
        }

        if (event.includeServer()) {
            generator.addProvider(true, new Recipes(generator));
            generator.addProvider(true, new TagGenerator(generator, existingFileHelper));
        }
    }

    public static final class ItemModels extends ItemModelProvider {
        public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, Snad.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            withExistingParent("snad", modLoc("block/snad"));
            withExistingParent("red_snad", modLoc("block/red_snad"));
            withExistingParent("suol_snad", modLoc("block/suol_snad"));
        }
    }

    public static final class BlockStates extends BlockStateProvider {
        public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, Snad.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(BlockRegistry.SNAD.get());
            simpleBlock(BlockRegistry.RED_SNAD.get());
            simpleBlock(BlockRegistry.SUOL_SNAD.get());
        }
    }

    public static final class TagGenerator extends BlockTagsProvider {
        public TagGenerator(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
            super(dataGenerator, Snad.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            this.tag(Snad.SNAD_BLOCKS).add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get(), BlockRegistry.SUOL_SNAD.get());

            this.tag(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("cactus_plantable_on")))
                    .add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get());

            this.tag(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("nether_wart_plantable_on")))
                    .add(BlockRegistry.SUOL_SNAD.get());

            this.tag(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("sugar_cane_plantable_on")))
                    .add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get());

            this.tag(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("bamboo_plantable_on")))
                    .add(BlockRegistry.SNAD.get(), BlockRegistry.RED_SNAD.get());
        }
    }

    public static final class Recipes extends RecipeProvider {
        public Recipes(DataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
            ShapelessRecipeBuilder.shapeless(BlockRegistry.RED_SNAD.get())
                    .requires(Items.RED_SAND)
                    .requires(Items.RED_SAND)
                    .unlockedBy("has_red_sand", has(Items.RED_SAND))
                    .save(consumer);

            ShapelessRecipeBuilder.shapeless(BlockRegistry.SNAD.get())
                    .requires(Items.SAND)
                    .requires(Items.SAND)
                    .unlockedBy("has_sand", has(Items.SAND))
                    .save(consumer);

            ShapelessRecipeBuilder.shapeless(BlockRegistry.SUOL_SNAD.get())
                    .requires(Items.SOUL_SAND)
                    .requires(Items.SOUL_SAND)
                    .unlockedBy("has_soul_sand", has(Items.SOUL_SAND))
                    .save(consumer);
        }
    }

    public static final class Lang extends LanguageProvider {
        public Lang(DataGenerator gen) {
            super(gen, Snad.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            this.add("block.snad.snad", "Snad");
            this.add("block.snad.red_snad", "Red Snad");
            this.add("block.snad.suol_snad", "Suol Snad");
        }
    }
}
