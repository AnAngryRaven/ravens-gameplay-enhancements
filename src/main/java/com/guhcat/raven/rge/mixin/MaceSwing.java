package com.guhcat.raven.rge.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.item.MaceItem.shouldDealAdditionalDamage;

@Mixin(MaceItem.class)
public abstract class MaceSwing {

    @Shadow @Nullable public abstract DamageSource getDamageSource(LivingEntity user);

    @Inject(method = "postHit", at = @At("HEAD"))
    public void SwingInject(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir){
        if(!shouldDealAdditionalDamage(attacker)){
            target.getPos().getHorizontal();
            World world = target.getWorld();
            List<? extends Entity> entitiesAround = world.getEntitiesByClass(LivingEntity.class, new Box(target.getX(), target.getY(), target.getZ() - 1, target.getX()+1.5, target.getY()+1, target.getZ() + 1.5), EntityPredicates.CAN_HIT);
            for (Entity e : entitiesAround){
                e.damage((ServerWorld) target.getWorld(), attacker.getDamageSources().mobAttack(attacker), 1.0F);
            }
        }
    }
}
