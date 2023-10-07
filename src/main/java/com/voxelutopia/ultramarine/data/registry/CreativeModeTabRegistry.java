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
}
