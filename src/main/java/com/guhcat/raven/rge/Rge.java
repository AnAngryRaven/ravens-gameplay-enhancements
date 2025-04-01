package com.guhcat.raven.rge;

import com.guhcat.raven.rge.datagen.LootTableModifier;
import com.guhcat.raven.rge.effects.ChoralEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetItemLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class Rge implements ModInitializer {

    //todo!();
    // - Configurable options
    //   - Baked-in difficulty based ones that are overridden by a main user-editable config? Ideally loaded on difficulty change; No idea if doable.

    public static final RegistryEntry<StatusEffect> CHORAL_EFFECT = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("rge", "choral"), new ChoralEffect());

    public static final Potion CHORAL = Registry.register(
            Registries.POTION,
            Identifier.of("rge", "choral"),
            new Potion("choral",
                    new StatusEffectInstance(CHORAL_EFFECT, 1, 0)));

    public static final Potion STRONG_CHORAL = Registry.register(
            Registries.POTION,
            Identifier.of("rge", "strong_choral"),
            new Potion("choral",
                    new StatusEffectInstance(CHORAL_EFFECT, 1, 1)));

    @Override
    public void onInitialize() {
        //Choral potion recipe
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    Potions.AWKWARD,
                    Items.CHORUS_FRUIT,
                    Registries.POTION.getEntry(CHORAL)
            );
        });

        //Choral potion II recipe
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    Registries.POTION.getEntry(CHORAL),
                    Items.GLOWSTONE,
                    Registries.POTION.getEntry(STRONG_CHORAL)
            );
        });

        EnchantmentList.initialize();
        LootTableModifier.init();
    }
}