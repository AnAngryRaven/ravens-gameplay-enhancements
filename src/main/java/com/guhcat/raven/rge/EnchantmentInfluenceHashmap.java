package com.guhcat.raven.rge;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashMap;

public interface EnchantmentInfluenceHashmap {
    HashMap<RegistryEntry<Enchantment>, Integer> enchantmentInfluence = new HashMap<RegistryEntry<Enchantment>, Integer>();
}
