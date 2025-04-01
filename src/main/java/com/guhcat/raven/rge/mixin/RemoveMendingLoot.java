package com.guhcat.raven.rge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantRandomlyLootFunction.class)
public abstract class RemoveMendingLoot {

    @ModifyVariable(method="process", at = @At("STORE"), ordinal = 0)
    public List<RegistryEntry<Enchantment>> removeMendingFromList(List<RegistryEntry<Enchantment>> list, @Local(argsOnly = true) LootContext context){
        if(list.size() == 1)
            return list;

        ArrayList<RegistryEntry<Enchantment>> newList = new ArrayList<>();
        for(RegistryEntry<Enchantment> e : list){
            if(e != context.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.MENDING))
                newList.add(e);
        }
        return newList.stream().toList();
    }
}
