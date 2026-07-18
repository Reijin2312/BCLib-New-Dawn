package org.betterx.bclib.registry;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.blockentities.BaseBarrelBlockEntity;
import org.betterx.bclib.blockentities.BaseChestBlockEntity;
import org.betterx.bclib.blockentities.BaseFurnaceBlockEntity;
import org.betterx.bclib.blockentities.DynamicBlockEntityType;
import org.betterx.bclib.blockentities.DynamicBlockEntityType.BlockEntitySupplier;
import org.betterx.bclib.blocks.BaseBarrelBlock;
import org.betterx.bclib.blocks.BaseChestBlock;
import org.betterx.bclib.blocks.BaseFurnaceBlock;
import org.betterx.bclib.blocks.signs.BaseSignBlock;
import org.betterx.bclib.furniture.entity.EntityChair;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseBlockEntities {
    public static final DynamicBlockEntityType<BaseChestBlockEntity> CHEST = registerBlockEntityType(BCLib.makeID(
            "chest"), BaseChestBlockEntity::new);
    public static final DynamicBlockEntityType<BaseBarrelBlockEntity> BARREL = registerBlockEntityType(BCLib.makeID(
            "barrel"), BaseBarrelBlockEntity::new);

    public static final DynamicBlockEntityType<BaseFurnaceBlockEntity> FURNACE = registerBlockEntityType(BCLib.makeID(
            "furnace"), BaseFurnaceBlockEntity::new);

    public static final EntityType<EntityChair> CHAIR = registerEntity(BCLib.makeID("chair"), EntityType.Builder
            .of(EntityChair::new, MobCategory.MISC)
            .sized(0.5F, 0.8F)
            .fireImmune()
            .noSummon()
            .build(ResourceKey.create(Registries.ENTITY_TYPE, BCLib.makeID("chair"))));


    public static <T extends Entity> EntityType<T> registerEntity(
            Identifier id,
            EntityType<T> entity
    ) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, id, entity);
        return entity;
    }

    public static <T extends BlockEntity> DynamicBlockEntityType<T> registerBlockEntityType(
            Identifier typeId,
            BlockEntitySupplier<? extends T> supplier
    ) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, typeId, new DynamicBlockEntityType<>(supplier));
    }

    public static void register() {
    }

    public static void registerChestBlock(Block block) {
        CHEST.registerBlock(block);
    }

    public static void registerBarrelBlock(Block block) {
        BARREL.registerBlock(block);
    }

    public static void registerFurnaceBlock(Block block) {
        FURNACE.registerBlock(block);
    }

    public static Block[] getChests() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseChestBlock)
                .toArray(Block[]::new);
    }

    public static Block[] getBarrels() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseBarrelBlock)
                .toArray(Block[]::new);
    }

    public static Block[] getSigns() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseSignBlock)
                .toArray(Block[]::new);
    }

    public static Block[] getFurnaces() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseFurnaceBlock)
                .toArray(Block[]::new);
    }
}
