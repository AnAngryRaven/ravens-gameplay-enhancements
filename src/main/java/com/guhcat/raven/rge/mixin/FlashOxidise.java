package com.guhcat.raven.rge.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Oxidizable;
import net.minecraft.entity.LightningEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LightningEntity.class)
public abstract class FlashOxidise {

    @Inject(method = "cleanOxidation", at = @At("HEAD"))
    private static void galvanicCorrosion(World world, BlockPos pos, CallbackInfo ci){
        BlockPos lowerPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        if(world.getBlockState(pos).getBlock() == Blocks.LIGHTNING_ROD && world.getBlockState(lowerPos).getBlock() == Blocks.GOLD_BLOCK){
            for(BlockPos loc : BlockPos.iterateOutwards(lowerPos, 1, 1, 1)){
                if(world.getBlockState(loc).getBlock() instanceof Oxidizable){
                    Optional<Block> nextOxidisableBlock = Oxidizable.getIncreasedOxidationBlock(world.getBlockState(loc).getBlock());
                    boolean cont = true;

                    while(cont && world.random.nextInt(10) > 4){
                        cont = nextOxidisableBlock.isPresent();

                        nextOxidisableBlock.ifPresent(block -> world.setBlockState(loc, block.getDefaultState()));
                        world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, loc, -1);
                    }

                    if(world.getDifficulty().equals(Difficulty.HARD) && !cont) {
                        world.removeBlock(loc, false);
                        world.getBlockState(loc).getBlock();
                        world.playSound(null, loc, SoundEvents.BLOCK_COPPER_BULB_BREAK, SoundCategory.BLOCKS, 25.0F, 1.0F);
                    }
                }
            }
        }
    }
}
