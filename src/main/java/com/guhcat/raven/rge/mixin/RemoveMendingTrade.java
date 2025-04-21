package com.guhcat.raven.rge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(TradeOffers.EnchantBookFactory.class)
public abstract class RemoveMendingTrade {

    @Shadow @Final
    private TagKey<Enchantment> possibleEnchantments;

    //todo!();
    // - Make removeMending configurable with multiple other enchantments?
    //    - Might be worthwhile increasing the number of rolls if so.
    //    - Regardless, will probably need a caveat telling the user not to put in too many; Gonna get a *lot* of vanishing books otherwise :P

    /**
     * Prevents mending from showing up as a librarian trade on normal or hard mode. Rolls a maximum of 10
     * times to generate another enchantment; If no other enchantment is generated, then curse of vanishing
     * is used in its place instead, to avoid locking up the game.
     * @param entity Entity with the trade offer
     * @param random Minecraft random util instance
     * @param cir
     * @param optional [UNUSED] LocalCapture variable
     * @param registryEntry LocalCapture variable
     * @param enchantment LocalCapture variable
     */
    @Inject(method = "create", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void removeMending(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir, Optional<RegistryEntry<Enchantment>> optional, @Local LocalRef<RegistryEntry<Enchantment>> registryEntry, @Local LocalRef<Enchantment> enchantment) {
        if (entity.getWorld().getDifficulty().equals(Difficulty.HARD) || entity.getWorld().getDifficulty().equals(Difficulty.NORMAL)) {
            //Helper variables; Just makes things look a bit tidier.
            Registry<Enchantment> registryManager = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            RegistryEntry<Enchantment> vanishing = registryManager.getEntry(registryManager.get(Enchantments.VANISHING_CURSE));

            //I'd like to give it a sporting chance to generate any enchantment *other* than mending.
            for (int a = 0; a < 100 && registryEntry.get().matchesKey(Enchantments.MENDING); a++)
                registryEntry.set(registryManager.getRandomEntry(this.possibleEnchantments, random).orElse(vanishing));

            //That being said, I'd rather not lock up the game for an eternity; Your prize for 10/10 rolls is curse of vanishing :D
            if (registryEntry.get().matchesKey(Enchantments.MENDING))
                registryEntry.set(vanishing);

            enchantment.set(registryEntry.get().value());
        }
    }
}
