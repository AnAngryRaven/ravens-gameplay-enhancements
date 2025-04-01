package com.guhcat.raven.rge.mixin;

import com.guhcat.raven.rge.EnchantmentInfluenceHashmap;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantingTableInfluencing extends ScreenHandler implements EnchantmentInfluenceHashmap {

    protected EnchantingTableInfluencing(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }


    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;setSeed(J)V"))
    public void getNumberOfChiseledBookshelves(ItemStack itemStack, World world, BlockPos pos, CallbackInfo ci, @Local LocalIntRef ix){
        enchantmentInfluence.clear();
        double chiselPower = 0;

        //Yes, this looks terrible. I know. I'm very tired right now and could use a break, so I'm afraid you're just going to have to deal with it.
        for(BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS){
            if(world.getBlockState(pos.add(blockPos.getX(), blockPos.getY(), blockPos.getZ())).getBlock() != Blocks.CHISELED_BOOKSHELF)
                continue;

            ChiseledBookshelfBlockEntity blockEntity = (ChiseledBookshelfBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos.add(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
            chiselPower += blockEntity.getFilledSlotCount() / 6.0F;

            for(int slotIndex = 0; slotIndex < ChiseledBookshelfBlockEntity.MAX_BOOKS; slotIndex++){
                ItemStack bookStack = blockEntity.getStack(slotIndex);

                if(bookStack.isOf(Items.ENCHANTED_BOOK))
                    continue;

                for (RegistryEntry<Enchantment> e : bookStack.getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS).getEnchantments()) {
                    enchantmentInfluence.put(e, enchantmentInfluence.getOrDefault(e, 0) + 1);
                }
            }
        }
        ix.set(ix.get() + (int) chiselPower);

        System.out.println(enchantmentInfluence);
    }


    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;generateEnchantments(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;II)Ljava/util/List;", shift = At.Shift.BY, by = 2))
    public void influenceEnchants(ItemStack itemStack, World world, BlockPos pos, CallbackInfo ci, @Local List<EnchantmentLevelEntry> list){
        if(!enchantmentInfluence.isEmpty()){
            //todo!(); Actually influence enchantments
        }
    }
}
