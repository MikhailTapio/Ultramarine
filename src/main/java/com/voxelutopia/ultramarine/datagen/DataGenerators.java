package com.voxelutopia.ultramarine.datagen;

import com.voxelutopia.ultramarine.Ultramarine;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = DataGenerators.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    public static final String MOD_ID = Ultramarine.MOD_ID;

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput(); // Use no arg for consistency previous versions
        ExistingFileHelper fh = event.getExistingFileHelper();
        BlockTagsProvider blockTags = new ModBlockTagProvider(output, event.getLookupProvider(), fh);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModItemTagProvider(output, event.getLookupProvider(), blockTags.contentsGetter(), fh));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, Set.of(), VanillaLootTableProvider.create(output).getTables()));
        // TODO: Needs check
        generator.addProvider(event.includeClient(), new ModBlockModelProvider(output, fh));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, fh));
    }
}
