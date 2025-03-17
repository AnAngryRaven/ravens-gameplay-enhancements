package com.guhcat.raven.rge.mixin;


//guh
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AnvilScreenHandler.class)
public abstract class anvil extends ForgingScreenHandler{

    public anvil(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager manager) {
        super(type, syncId, playerInventory, context, manager);
    }

    @Shadow
    private int repairItemUsage;

    @Shadow @Final private Property levelCost;
    private boolean applyToL;
    private boolean canTake;
    private boolean isRenaming;

    //@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AnvilScreenHandler;sendContentUpdates()V"), method="updateResult()V", locals = LocalCapture.CAPTURE_FAILHARD)
    @ModifyVariable(at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"), method="updateResult()V", ordinal = 0)
    public int setI(int i){
        applyToL = true;
        canTake = true;
        return 1;
    }

    @ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;remove(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"), method="updateResult()V", ordinal = 0)
    public int setWhenRemovingName(int i){
        if(applyToL){
            i -= 1;
            return i;
        }else{
            return i;
        }
    }

    @ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;"), method="updateResult()V", ordinal = 0)
    public int setWhenRenaming(int i){
        if(applyToL){
            i -= 1;
            return i;
        }else{
            return i;
        }
    }

    @ModifyVariable(at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"), method="updateResult()V", ordinal = 1)
    public int setJ(int j){
        return 1;
    }

    //@ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(JJJ)J", shift=At.Shift.AFTER), method="updateResult()V", ordinal = 10)
    @ModifyVariable(at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"), method="updateResult()V", ordinal = 0)
    public long setPriceToFree(long l){

        if(applyToL){
            applyToL = false;
            return -1;
        }else if (isRenaming) {
            isRenaming = false;
            return -1;
        }else {
            return l;
        }
    }

    /**
     * @author AnAngryRaven
     * @reason The game will refuse to output anything that has a levelCost of zero. Given this is a single line return statement, it needs to be modified to check that if it's set to zero, it's because the player is repairing their item.
     */
    @Overwrite
    public boolean canTakeOutput(PlayerEntity player, boolean present) {
        return (player.isInCreativeMode() || player.experienceLevel > this.levelCost.get()) && (canTake || this.levelCost.get() > 0);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AnvilScreenHandler;sendContentUpdates()V"), method="updateResult()V", locals = LocalCapture.CAPTURE_FAILHARD)
    public void guh(CallbackInfo ci, ItemStack itemStack, int i, long l, int j, ItemStack itemStack2){
        System.out.println("int i = "+i);
        System.out.println("int j = "+j);
        System.out.println("int l = "+l);
        if(i == j && l > -1){
            isRenaming = true;
        }
    }
}
