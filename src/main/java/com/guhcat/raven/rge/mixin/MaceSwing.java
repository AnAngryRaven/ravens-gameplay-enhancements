package com.guhcat.raven.rge.mixin;

import com.guhcat.raven.rge.EnchantmentList;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class MaceSwing extends LivingEntity {

    @Shadow public abstract void spawnSweepAttackParticles();

    protected MaceSwing(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void SwingInject(Entity target, CallbackInfo ci, float f, ItemStack itemStack, DamageSource damageSource, float g, float h){
        if(itemStack.isOf(Items.MACE) && !MaceItem.shouldDealAdditionalDamage(this)){
            World world = this.getWorld();
            int densityEnchantLevel = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.DENSITY), itemStack);
            int lightweightEnchantLevel = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(EnchantmentList.LIGHTWEIGHT_KEY), itemStack);
            float damageDealt = ((densityEnchantLevel*0.5F) + ((f / 2) * h));

            List<? extends Entity> entitiesAround = world.getEntitiesByClass(LivingEntity.class, new Box(target.getX() - (1 + (lightweightEnchantLevel * 0.25)), target.getY(), target.getZ() - (1 + (lightweightEnchantLevel * 0.25)), target.getX()+(1 + (lightweightEnchantLevel * 0.25)), target.getY()+1, target.getZ() + (1 + (lightweightEnchantLevel * 0.25))), EntityPredicates.CAN_HIT);

            if(this.getWorld() instanceof ServerWorld serverWorld) {
                world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 0.1F);
                this.spawnSweepAttackParticles();
                for (Entity e : entitiesAround) {
                    if (e != this && e != target)
                        e.damage(serverWorld, damageSource, damageDealt);
                }
            }
        }
    }
}
