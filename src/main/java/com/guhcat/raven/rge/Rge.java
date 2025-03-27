package com.guhcat.raven.rge;

import com.guhcat.raven.rge.effects.ChoralEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class Rge implements ModInitializer {

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
    }
}
