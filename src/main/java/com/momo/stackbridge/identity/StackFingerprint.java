package com.momo.stackbridge.identity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public record StackFingerprint(Identifier itemId, int componentsHash) {
    public static StackFingerprint of(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return new StackFingerprint(Registries.ITEM.getId(Items.AIR), 0);
        }
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        int componentsHash = StackNormalizer.normalizedComponentsHash(stack);
        return new StackFingerprint(itemId, componentsHash);
    }
}
