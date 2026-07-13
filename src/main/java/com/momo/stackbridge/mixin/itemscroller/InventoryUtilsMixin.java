package com.momo.stackbridge.mixin.itemscroller;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.ItemStack;

import com.momo.stackbridge.identity.StackNormalizer;

@Mixin(targets = "fi.dy.masa.itemscroller.util.InventoryUtils", remap = false)
public abstract class InventoryUtilsMixin {
    @Inject(method = "areStacksEqual", at = @At("HEAD"), cancellable = true, remap = false)
    private static void stackidentitybridge$compareNormalized(ItemStack stack1, ItemStack stack2, CallbackInfoReturnable<Boolean> cir) {
        if (StackNormalizer.sameIdentity(stack1, stack2)) {
            cir.setReturnValue(true);
        }
    }
}
