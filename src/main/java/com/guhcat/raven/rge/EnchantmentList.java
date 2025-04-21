package com.guhcat.raven.rge;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EnchantmentList {

    public static final RegistryKey<Enchantment> LIGHTWEIGHT_KEY = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("rge", "lightweight"));

    public static void initialize(){  };
}
