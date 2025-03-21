package com.guhcat.raven.rge.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.consume.TeleportRandomlyConsumeEffect;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class ChoralEffect extends InstantStatusEffect {
    private boolean isApplied;
    public ChoralEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xce0dd1);
        isApplied = false;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if(isApplied)
            return super.applyUpdateEffect(world, entity, amplifier); //For whatever reason it applies twice without this; not sure if I have to do something else

        TeleportRandomlyConsumeEffect teleport = new TeleportRandomlyConsumeEffect(16+(amplifier*8));
        boolean _b = teleport.onConsume(world, null, entity);
        return super.applyUpdateEffect(world, entity, amplifier);
    }

    @Override
    public void applyInstantEffect(ServerWorld world, @Nullable Entity effectEntity, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        TeleportRandomlyConsumeEffect teleport = new TeleportRandomlyConsumeEffect((int)(proximity * (16+(amplifier*8))));
        boolean _b = teleport.onConsume(world, null, target);
        isApplied = true;
        super.applyInstantEffect(world, effectEntity, attacker, target, amplifier, proximity);
    }

    
    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        isApplied = false;
        super.onApplied(entity, amplifier);
    }
}
