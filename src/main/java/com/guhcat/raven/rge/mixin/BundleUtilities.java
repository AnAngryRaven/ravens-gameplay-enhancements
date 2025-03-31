package com.guhcat.raven.rge.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleContentsComponent.class)
public abstract class BundleUtilities {
    @Inject(method = "getOccupancy(Lnet/minecraft/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;", at = @At("HEAD"), cancellable = true)
    private static void changeUtilityMax(ItemStack itemStack, CallbackInfoReturnable<Fraction> cir){
        boolean isUtility = itemStack.isOf(Items.FLINT_AND_STEEL) ||
                itemStack.isOf(Items.SHEARS) ||
                itemStack.isOf(Items.SPYGLASS) ||
                itemStack.isOf(Items.BRUSH);
        if(isUtility){
            cir.setReturnValue(Fraction.getFraction(1,16));
        }
    }
}
