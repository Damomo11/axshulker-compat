package com.momo.stackbridge.identity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record StackFingerprint(Identifier itemId, int componentsHash) {
    public static StackFingerprint of(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return new StackFingerprint(BuiltInRegistries.ITEM.getKey(Items.AIR), 0);
        }
        Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        int componentsHash = StackNormalizer.normalizedComponentsHash(stack);
        return new StackFingerprint(itemId, componentsHash);
    }
}
