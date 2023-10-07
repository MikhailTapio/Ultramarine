package com.voxelutopia.ultramarine.datagen;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.world.block.StackableHalfBlock;
import com.voxelutopia.ultramarine.world.block.state.ModBlockStateProperties;
import com.voxelutopia.ultramarine.world.block.state.StackableBlockType;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class BaseLootTableProvider extends LootTableProvider {

    private static final Logger LOGGER = Ultramarine.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.BEDROCK).map(ItemLike::asItem).collect(ImmutableSet.toImmutableSet());
    ;
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private final PackOutput.PathProvider provider;

    public BaseLootTableProvider(PackOutput pOutput, Set<ResourceLocation> pRequiredTables, List<SubProviderEntry> pSubProviders) {
        super(pOutput, pRequiredTables, pSubProviders);
        this.provider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
    }

    protected abstract void addTables();

    protected LootTable.Builder createSimpleTable(String name, ItemLike block) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block));
        return LootTable.lootTable().withPool(builder);
    }

    //todo add fortune modifier
    protected LootTable.Builder createSingleItemTable(String name, Block block, Item drops) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(drops));
        return LootTable.lootTable().withPool(builder);
    }

    protected LootTable.Builder createSlabDrop(String name, SlabBlock block, Item item) {
        var builder = LootPool.lootPool()
                .name(name)
                .add(LootItem.lootTableItem(item)
                        .when(LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder
                                        .properties()
                                        .hasProperty(SlabBlock.TYPE, SlabType.TOP))))
                .add(LootItem.lootTableItem(item)
                        .when(LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder
                                        .properties()
                                        .hasProperty(SlabBlock.TYPE, SlabType.BOTTOM))))
                .add(LootItem.lootTableItem(item)
                        .when(LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder
                                        .properties()
                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))));
        return LootTable.lootTable().withPool(builder);
    }

    protected LootTable.Builder createStackableHalfDrop(String name, StackableHalfBlock block, Item item) {
        var builder = LootPool.lootPool()
                .name(name)
                .add(LootItem.lootTableItem(item)
                        .when(LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder
                                        .properties()
                                        .hasProperty(ModBlockStateProperties.STACKABLE_BLOCK_TYPE, StackableBlockType.SINGLE))))
                .add(LootItem.lootTableItem(item)
                        .when(LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder
                                        .properties()
                                        .hasProperty(ModBlockStateProperties.STACKABLE_BLOCK_TYPE, StackableBlockType.DOUBLE)))
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))));
        return LootTable.lootTable().withPool(builder);
    }

    protected static LootTable.Builder createAbundantOreDrop(String name, Block block, Item drop, float min, float max) {
        return createSilkTouchDispatchTable(block, name, applyExplosionDecay(block, LootItem.lootTableItem(drop)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected static LootTable.Builder createOreDrop(String name, Block block, Item drop) {
        return createSilkTouchDispatchTable(block, name, applyExplosionDecay(block, LootItem.lootTableItem(drop).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }


    protected static LootTable.Builder createPorcelainDrop(String name, Block block, Item piece, Item shard) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().name(name)
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(block).when(HAS_SILK_TOUCH)
                .otherwise(LootItem.lootTableItem(piece).when(LootItemRandomChanceCondition.randomChance(0.01F)).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2)))
                .otherwise(LootItem.lootTableItem(shard).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))));
    }

    protected static LootTable.Builder createSelfDropDispatchTable(Block pBlock, String name, LootItemCondition.Builder pConditionBuilder, LootPoolEntryContainer.Builder<?> pAlternativeEntryBuilder) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().name(name)
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(pBlock).when(pConditionBuilder)
                .otherwise(pAlternativeEntryBuilder)));
    }

    protected static LootTable.Builder createSilkTouchDispatchTable(Block pBlock, String name, LootPoolEntryContainer.Builder<?> pAlternativeEntryBuilder) {
        return createSelfDropDispatchTable(pBlock, name, HAS_SILK_TOUCH, pAlternativeEntryBuilder);
    }

    protected static <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike pItem, FunctionUserBuilder<T> pFunction) {
        return (!EXPLOSION_RESISTANT.contains(pItem.asItem()) ? pFunction.apply(ApplyExplosionDecay.explosionDecay()) : pFunction.unwrap());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        addTables();

        return CompletableFuture.allOf(lootTables.entrySet().stream().map(e -> {
            ResourceLocation r = e.getKey().getLootTable();
            LootTable l = e.getValue().setParamSet(LootContextParamSets.BLOCK).build();
            Path path = this.provider.json(r);
            // TODO: Needs check
            return DataProvider.saveStable(cache, LootDataType.TABLE.parser().toJsonTree(l), path);
        }).toArray(CompletableFuture[]::new));
    }


    //private void writeTables(CachedOutput cache, Map<ResourceLocation, LootTable> tables) {
    //    Path outputFolder = this.generator.getPackOutput().getOutputFolder();
    //    tables.forEach((key, lootTable) -> {
    //        Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
    //        DataProvider.saveStable(cache, LootDataType.TABLE.parser().toJsonTree(lootTable), path);
    //    });
    //}

    // TODO: Extracted to this#run, needs an extra check

    //@Override
    //public String getName() {
    //    return DataGenerators.MOD_ID +  " LootTables";
    //}

    //TODO: Any alternative needed to be overriden?
}
