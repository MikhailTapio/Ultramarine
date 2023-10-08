package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.data.ModCreativeTab;
import com.voxelutopia.ultramarine.world.item.BaseFood;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ultramarine.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MATERIALS = TABS.register("materials", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".materials"))
            .icon(() -> new ItemStack(ItemRegistry.CYAN_BRICK.get()))
            .displayItems((param, list) -> {
                list.acceptAll(ForgeRegistries.ITEMS.getValues().stream()
                        .filter(i -> i instanceof BaseFood).map(Item::getDefaultInstance)
                        .toList());
                list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.MATERIALS).stream().map(r -> r.get().getDefaultInstance()).toList());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> TOOLS = TABS.register("tools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".tools"))
            .icon(() -> new ItemStack(ItemRegistry.WOODEN_HAMMER.get()))
            .displayItems((param, list) -> {
                list.accept(ItemRegistry.WOODEN_HAMMER.get());
                list.accept(ItemRegistry.WOODWORKING_WORKBENCH.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> BUILDING_BLOCKS = TABS.register("building_blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".building_blocks"))
            .icon(() -> new ItemStack(ItemRegistry.CYAN_BRICKS.get()))
            .displayItems((param, list) -> list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.BUILDING_BLOCKS).stream()
                    .map(r -> r.get().getDefaultInstance()).toList()))
            .build());

    public static final RegistryObject<CreativeModeTab> DECORATIVE_BLOCKS = TABS.register("decorative_blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".decorative_blocks"))
            .icon(() -> new ItemStack(ItemRegistry.CARVED_RED_PILLAR_BASE.get()))
            .displayItems((param, list) -> list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.DECORATIVE_BLOCKS).stream()
                    .map(r -> r.get().getDefaultInstance()).toList()))
            .build());

    public static final RegistryObject<CreativeModeTab> DECORATIONS = TABS.register("decorations", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".decorations"))
            .icon(() -> new ItemStack(ItemRegistry.LARGE_BLUE_AND_WHITE_PORCELAIN_VASE.get()))
            .displayItems((param, list) -> list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.DECORATIONS).stream()
                    .map(r -> r.get().getDefaultInstance()).toList()))
            .build());

    public static final RegistryObject<CreativeModeTab> FURNITURE = TABS.register("furniture", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".furniture"))
            .icon(() -> new ItemStack(ItemRegistry.OAK_CABINET.get()))
            .displayItems((param, list) -> list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.FURNITURE).stream()
                    .map(r -> r.get().getDefaultInstance()).toList()))
            .build());

    public static final RegistryObject<CreativeModeTab> WINDOWS_AND_DOORS = TABS.register("windows_and_doors", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".windows_and_doors"))
            .icon(() -> new ItemStack(ItemRegistry.CARVED_WOODEN_DOOR.get()))
            .displayItems((param, list) -> list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.WINDOWS_AND_DOORS).stream()
                    .map(r -> r.get().getDefaultInstance()).toList()))
            .build());

    public static final RegistryObject<CreativeModeTab> PLANTS = TABS.register("plants", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".plants"))
            .icon(() -> new ItemStack(ItemRegistry.MEDIUM_LOTUS.get()))
            .displayItems((param, list) -> list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.PLANTS).stream()
                    .map(r -> r.get().getDefaultInstance()).toList()))
            .build());

    public static final RegistryObject<CreativeModeTab> LAMPS = TABS.register("lamps", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Ultramarine.MOD_ID + ".lamps"))
            .icon(() -> new ItemStack(ItemRegistry.OCTAGONAL_PALACE_LANTERN.get()))
            .displayItems((param, list) -> list.acceptAll(ModCreativeTab.getItemSet(ModCreativeTab.LAMPS).stream()
                    .map(r -> r.get().getDefaultInstance()).toList()))
            .build());
}
