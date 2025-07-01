package com.guhcat.raven.rge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.HungerConstants;
import net.minecraft.entity.player.HungerManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class SaturationOverfill {

    @Shadow
    private float saturationLevel;

    @Shadow
    private int foodLevel;

    @Inject(method="addInternal", at = @At("HEAD"))
    private void calculateOverfill(int nutrition, float saturation, CallbackInfo ci) {
        float new_saturation = Math.clamp(((nutrition + this.foodLevel) - HungerConstants.FULL_FOOD_LEVEL), 0.0F, HungerConstants.FULL_FOOD_LEVEL);
        this.saturationLevel = Math.clamp(new_saturation + saturation + this.saturationLevel, 0.0F, Math.clamp(this.foodLevel + nutrition, 0.0F, HungerConstants.FULL_FOOD_LEVEL));
    }

    @Redirect(method = "addInternal(IF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/HungerManager;saturationLevel:F", opcode = Opcodes.PUTFIELD))
    private void saturation(HungerManager instance, float value, @Local(argsOnly = true) int nutrition){
        this.saturationLevel = this.saturationLevel;
    }
}
