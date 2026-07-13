package com.momo.stackbridge.mixin.ipn;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.item.Item;

import com.momo.stackbridge.identity.StackNormalizer;
import com.momo.stackbridge.integration.ItemTypeBridgeAccess;

@Mixin(targets = "org.anti_ad.mc.ipnext.item.ItemType", remap = false)
public abstract class ItemTypeMixin implements ItemTypeBridgeAccess {
    @Shadow(remap = false)
    public abstract Item getItem();

    @Shadow(remap = false)
    public abstract MergedComponentMap getTag();

    @Inject(method = "equals", at = @At("HEAD"), cancellable = true, remap = false)
    private void stackidentitybridge$equals(Object other, CallbackInfoReturnable<Boolean> cir) {
        if (!(other instanceof ItemTypeBridgeAccess otherAccess)) {
            return;
        }
        Item item = this.stackidentitybridge$item();
        Item otherItem = otherAccess.stackidentitybridge$item();
        boolean shulker = StackNormalizer.isShulkerBoxItem(item);
        boolean otherShulker = StackNormalizer.isShulkerBoxItem(otherItem);
        if (!shulker && !otherShulker) {
            return;
        }
        cir.setReturnValue(item == otherItem && StackNormalizer.normalizedComponentsEqual(
            this.stackidentitybridge$components(),
            otherAccess.stackidentitybridge$components(),
            true
        ));
    }

    @Inject(method = "hashCode", at = @At("HEAD"), cancellable = true, remap = false)
    private void stackidentitybridge$hashCode(CallbackInfoReturnable<Integer> cir) {
        Item item = this.stackidentitybridge$item();
        if (StackNormalizer.isShulkerBoxItem(item)) {
            int result = item.hashCode();
            result = 31 * result + StackNormalizer.normalizedComponentsHash(this.stackidentitybridge$components(), true);
            cir.setReturnValue(result);
        }
    }

    @Override
    public Item stackidentitybridge$item() {
        return this.getItem();
    }

    @Override
    public ComponentMap stackidentitybridge$components() {
        return this.getTag();
    }
}
