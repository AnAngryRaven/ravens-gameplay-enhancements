package com.guhcat.raven.rge.datagen;

import com.guhcat.raven.rge.EnchantmentList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.ItemTags;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnchantmentGenerator extends FabricDynamicRegistryProvider {

    public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        List<? extends RegistryEntry<Enchantment>> lightweightMutuallyExclusive = Arrays.asList(wrapperLookup.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.DENSITY), wrapperLookup.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.BREACH));

        register(entries, EnchantmentList.LIGHTWEIGHT_KEY, Enchantment.builder(
                Enchantment.definition(
                        wrapperLookup.getOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        5,
                        4,
                        Enchantment.leveledCost(1, 15),
                        Enchantment.leveledCost(1, 20),
                        10,
                        AttributeModifierSlot.HAND
                )
        ).exclusiveSet(RegistryEntryList.of(lightweightMutuallyExclusive)));
    }

    private void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions){
        entries.add(key, builder.build(key.getValue()), resourceConditions);
    }

    @Override
    public String getName() {
        return "RavensGameplayEnhancementsEnchantmentGenerator";
    }
}
