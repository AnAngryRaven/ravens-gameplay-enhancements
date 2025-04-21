package com.guhcat.raven.rge.mixin;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(Blocks.class)
public abstract class TorchflowerLuminance {

    @Shadow
    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) { throw new NotImplementedException(); };

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/Block;", ordinal = 100))
    private static Block redirectThingy(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings){

        return register(
                "torchflower",
                sets -> new FlowerBlock(StatusEffects.NIGHT_VISION, 5.0F, sets),
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.DARK_GREEN)
                        .noCollision()
                        .luminance(state -> 9)
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
                        .offset(AbstractBlock.OffsetType.XZ)
                        .pistonBehavior(PistonBehavior.DESTROY)
        );
    }

}
