package com.guhcat.raven.rge.mixin;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.item.MaceItem.shouldDealAdditionalDamage;

@Mixin(PlayerEntity.class)
public abstract class MaceSwing extends LivingEntity {

    protected MaceSwing(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void SwingInject(Entity target, CallbackInfo ci, float f, ItemStack itemStack, DamageSource damageSource, float g, float h){
        if(itemStack.isOf(Items.MACE) && !MaceItem.shouldDealAdditionalDamage(this) && this.getWorld() instanceof ServerWorld serverWorld){
            World world = this.getWorld();
            int densityEnchantLevel = EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.DENSITY), itemStack);
            float damageDealt = (float)((densityEnchantLevel*0.5F) + ((this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE) / 2) * h));
            List<? extends Entity> entitiesAround = world.getEntitiesByClass(LivingEntity.class, new Box(target.getX(), target.getY(), target.getZ() - 1, target.getX()+1.5, target.getY()+1, target.getZ() + 1.5), EntityPredicates.CAN_HIT);

            for (Entity e : entitiesAround) {
                if(e != this && e != target)
                    e.damage(serverWorld, damageSource, damageDealt);
            }
        }
    }
}
