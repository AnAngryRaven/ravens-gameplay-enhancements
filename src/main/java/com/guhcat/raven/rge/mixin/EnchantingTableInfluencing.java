package com.guhcat.raven.rge.mixin;

import com.guhcat.raven.rge.EnchantmentInfluenceHashmap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantingTableInfluencing extends ScreenHandler implements EnchantmentInfluenceHashmap {

    @Shadow @Final private Random random;

    @Shadow @Final public int[] enchantmentPower;

    int iter = 0;

    protected EnchantingTableInfluencing(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }


    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;setSeed(J)V"))
    public void getNumberOfChiseledBookshelves(ItemStack itemStack, World world, BlockPos pos, CallbackInfo ci, @Local LocalIntRef ix){
        enchantmentInfluence.clear();
        double chiselPower = 0;

        //Not sure what I can really do about this I fear :/
        for(BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS){
            if(world.getBlockState(pos.add(blockPos.getX(), blockPos.getY(), blockPos.getZ())).getBlock() != Blocks.CHISELED_BOOKSHELF)
                continue;

            ChiseledBookshelfBlockEntity blockEntity = (ChiseledBookshelfBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos.add(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
            chiselPower += blockEntity.getFilledSlotCount() / 6.0F;

            for(int slotIndex = 0; slotIndex < ChiseledBookshelfBlockEntity.MAX_BOOKS; slotIndex++){
                ItemStack bookStack = blockEntity.getStack(slotIndex);

                if(!bookStack.isOf(Items.ENCHANTED_BOOK))
                    continue;

                for (RegistryEntry<Enchantment> e : bookStack.getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS).getEnchantments()) {
                    if(e.isIn(EnchantmentTags.TREASURE) || (!e.value().isPrimaryItem(itemStack) && !itemStack.isOf(Items.BOOK)))
                        continue;

                    enchantmentInfluence.put(e, enchantmentInfluence.getOrDefault(e, 0) + bookStack.getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(e));
                }
            }
        }
        enchantmentInfluence.forEach((enchant, number) -> enchantmentInfluence.put(enchant, Math.min((int)(5*Math.log10(number) + 0.2), 10)));
        ix.set(ix.get() + (int) chiselPower);
    }


    @WrapOperation(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;generateEnchantments(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;II)Ljava/util/List;"))
    public List<EnchantmentLevelEntry> influenceEnchants(EnchantmentScreenHandler instance, DynamicRegistryManager registryManager, ItemStack stack, int slot, int level, Operation<List<EnchantmentLevelEntry>> original){
        return helperFunction(instance, registryManager, stack, slot, level, original);
    }

    @WrapOperation(method = "method_17410", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;generateEnchantments(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;II)Ljava/util/List;"))
    public List<EnchantmentLevelEntry> influenceApplied(EnchantmentScreenHandler instance, DynamicRegistryManager registryManager, ItemStack stack, int slot, int level, Operation<List<EnchantmentLevelEntry>> original){
        return helperFunction(instance, registryManager, stack, slot, level, original);
    }

    /**
     * If there are enchantments to influence, then combine all possible enchantments for the item with
     * influenced enchantments, and generate enchantments for the item.
     * Note that this function only really exists to prevent duplicate code; For whatever reason, the
     * vanilla implementation appears to have duplicated code for generating the enchantments and then
     * actually applying them.
     * @param instance Instance of EnchantmentScreenHandler
     * @param registryManager Registry manager of the world
     * @param stack Item to be enchanted
     * @param slot Enchanting table enchantment slot
     * @param level Level of the enchantment
     * @param original Original operation
     * @return List of influenced enchantments
     */
    @Unique
    public List<EnchantmentLevelEntry> helperFunction(EnchantmentScreenHandler instance, DynamicRegistryManager registryManager, ItemStack stack, int slot, int level, Operation<List<EnchantmentLevelEntry>> original) {
        if(enchantmentInfluence.isEmpty())
            return original.call(instance, registryManager, stack, slot, level);

        ArrayList<RegistryEntry<Enchantment>> tempList = new ArrayList<>();
        List<EnchantmentLevelEntry> returnList;

        enchantmentInfluence.forEach((enchant, number) -> {
            for(int i = 0; i < number; i++){
                tempList.add(enchant);
            }
        });
        original.call(instance, registryManager, stack, slot, level).forEach(e -> tempList.add(e.enchantment()));

        if((returnList = EnchantmentHelper.generateEnchantments(random, stack, level, tempList.stream())).size() > 1 && stack.isOf(Items.BOOK))
            returnList.remove(this.random.nextInt(returnList.size()));

        return returnList;
    }
}
