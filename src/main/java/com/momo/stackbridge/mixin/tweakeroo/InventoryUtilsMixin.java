package com.momo.stackbridge.mixin.tweakeroo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.item.ItemStack;

import com.momo.stackbridge.identity.StackNormalizer;

@Mixin(targets = "fi.dy.masa.tweakeroo.util.InventoryUtils", remap = false)
public abstract class InventoryUtilsMixin {
    @Redirect(
        method = {
            "preRestockHand",
            "findSlotWithItem"
        },
        at = @At(
            value = "INVOKE",
            target = "Lfi/dy/masa/malilib/util/InventoryUtils;areStacksEqualIgnoreDurability(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z",
            remap = false
        ),
        remap = false,
        require = 0
    )
    private static boolean stackidentitybridge$compareRestockStacks(ItemStack stack, ItemStack reference) {
        if (stackidentitybridge$areStacksEqualIgnoreDurability(stack, reference)) {
            return true;
        }
        if (stack == null || reference == null || stack.isEmpty() || reference.isEmpty()) {
            return false;
        }
        if (!StackNormalizer.isShulkerBox(stack) && !StackNormalizer.isShulkerBox(reference)) {
            return false;
        }
        return StackNormalizer.sameIdentity(stack, reference);
    }

    private static boolean stackidentitybridge$areStacksEqualIgnoreDurability(ItemStack stack, ItemStack reference) {
        if (stack == null || reference == null) {
            return false;
        }
        ItemStack first = stack.copy();
        ItemStack second = reference.copy();
        first.setCount(1);
        second.setCount(1);
        if (first.isDamageable() && first.isDamaged()) {
            first.setDamage(0);
        }
        if (second.isDamageable() && second.isDamaged()) {
            second.setDamage(0);
        }
        return ItemStack.areItemsAndComponentsEqual(first, second);
    }
}
