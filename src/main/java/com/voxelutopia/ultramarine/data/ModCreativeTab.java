package com.voxelutopia.ultramarine.data;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum ModCreativeTab {
    MATERIALS,
    TOOLS,
    BUILDING_BLOCKS,
    DECORATIVE_BLOCKS,
    DECORATIONS,
    FURNITURE,
    WINDOWS_AND_DOORS,
    PLANTS,
    LAMPS;

    static final Map<ModCreativeTab, Set<RegistryObject<Item>>> itemSets = Map.of(
            MATERIALS, new HashSet<>(),
            TOOLS, new HashSet<>(),
            BUILDING_BLOCKS, new HashSet<>(),
            DECORATIVE_BLOCKS, new HashSet<>(),
            DECORATIONS, new HashSet<>(),
            FURNITURE, new HashSet<>(),
            WINDOWS_AND_DOORS, new HashSet<>(),
            PLANTS, new HashSet<>(),
            LAMPS, new HashSet<>()
    );

    public static void putItemInSet(RegistryObject<Item> item, ModCreativeTab tab) {
        itemSets.get(tab).add(item);
    }

    public static Set<RegistryObject<Item>> getItemSet(ModCreativeTab tab) {
        return ImmutableSet.copyOf(itemSets.get(tab));
    }
}
