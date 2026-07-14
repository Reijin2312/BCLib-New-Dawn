package org.betterx.bclib.api.v3.datagen;

import org.betterx.wover.loot.api.BlockLootProvider;
import org.betterx.wover.loot.api.LootLookupProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BlockLootTableProvider extends SimpleFabricLootTableProvider {
    protected final List<String> modIDs;
    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    public BlockLootTableProvider(
            FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookup,
            List<String> modIDs
    ) {
        super(output, registryLookup, LootContextParamSets.BLOCK);
        this.modIDs = modIDs;
        this.registryLookup = registryLookup;
    }


    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        LootLookupProvider provider = new LootLookupProvider(registryLookup.join());
        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (id == null || !modIDs.contains(id.getNamespace())) {
                continue;
            }

            ResourceKey<LootTable> tableKey = ResourceKey.create(Registries.LOOT_TABLE, id.withPrefix("block/"));
            if (block instanceof BlockLootProvider blockLootProvider) {
                biConsumer.accept(tableKey, blockLootProvider.registerBlockLoot(id, provider, tableKey));
            } else if (block instanceof LootDropProvider dropper) {
                    LootTable.Builder builder = LootTable.lootTable();
                    dropper.getDroppedItemsBCL(builder);
                    biConsumer.accept(tableKey, builder);
            }
        }
    }
}
