package com.guhcat.raven.rge.datagen;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKeys;

public class LootTableModifier {

    public static void init(){
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (LootTables.ANCIENT_CITY_CHEST.equals(key) && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder().with(EmptyEntry.builder().weight(9)).with(ItemEntry.builder(Items.BOOK).weight(1)).apply(EnchantRandomlyLootFunction.create().option(registries.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.MENDING))).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).rolls(ConstantLootNumberProvider.create(1.0F));

                tableBuilder.pool(poolBuilder);
            }
        });
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (LootTables.END_CITY_TREASURE_CHEST.equals(key) && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder().with(EmptyEntry.builder().weight(95)).with(ItemEntry.builder(Items.BOOK).weight(5)).apply(EnchantRandomlyLootFunction.create().option(registries.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.MENDING))).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))).rolls(ConstantLootNumberProvider.create(1.0F));

                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
