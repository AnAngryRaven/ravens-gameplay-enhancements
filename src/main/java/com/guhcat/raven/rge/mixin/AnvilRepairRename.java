package com.guhcat.raven.rge.mixin;


import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.util.StringHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilRepairRename extends ForgingScreenHandler{

    public AnvilRepairRename(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager manager) {
        super(type, syncId, playerInventory, context, manager);
    }

    @Shadow @Final private Property levelCost;
    @Shadow private @Nullable String newItemName;

    @ModifyVariable(method = "updateResult()V", at = @At(value = "LOAD"), name = "t")
    public int setCost(int value){
        if(this.input.getStack(0).canRepairWith(this.input.getStack(1)) && !this.input.getStack(1).hasEnchantments())
            return 0;

        if(this.newItemName != null && !StringHelper.isBlank(this.newItemName)) {
            if(!this.input.getStack(1).isEmpty()){
                return value - 1;
            }else{
                return 0;
            }
        }

        return value;
    }
}
