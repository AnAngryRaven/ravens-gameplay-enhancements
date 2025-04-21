package com.guhcat.raven.rge.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(BundleContentsComponent.class)
public abstract class BundleUtilities {
    @Inject(method = "getOccupancy(Lnet/minecraft/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;", at = @At("HEAD"), cancellable = true)
    private static void changeUtilityMax(ItemStack itemStack, CallbackInfoReturnable<Fraction> cir){
        boolean isUtility = itemStack.streamTags().anyMatch(Predicate.isEqual(TagKey.of(RegistryKeys.ITEM, Identifier.of("rge", "utilities"))));
        if(isUtility){
            cir.setReturnValue(Fraction.getFraction(1,16));
        }
    }
}
