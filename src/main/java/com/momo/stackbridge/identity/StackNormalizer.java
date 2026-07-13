package com.momo.stackbridge.identity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import com.momo.stackbridge.config.BridgeConfig;

public final class StackNormalizer {
    private StackNormalizer() {
    }

    public static ItemStack normalize(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack copy = stack.copy();
        if (!BridgeConfig.current().enabled()) {
            return copy;
        }
        return copy;
    }

    public static boolean sameIdentity(ItemStack left, ItemStack right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        ItemStack leftNormalized = normalize(left);
        ItemStack rightNormalized = normalize(right);
        if (leftNormalized.isEmpty() || rightNormalized.isEmpty()) {
            return leftNormalized.isEmpty() && rightNormalized.isEmpty();
        }
        Item leftItem = leftNormalized.getItem();
        Item rightItem = rightNormalized.getItem();
        if (leftItem != rightItem) {
            return false;
        }
        return normalizedComponentsEqual(
            leftNormalized.getComponents(),
            rightNormalized.getComponents(),
            isShulkerBoxItem(leftItem)
        );
    }

    public static int normalizedComponentsHash(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        return normalizedComponentsHash(stack.getComponents(), isShulkerBox(stack));
    }

    public static int normalizedComponentsHash(DataComponentMap components, boolean ignoreCustomData) {
        if (components == null) {
            return 0;
        }
        int hash = 1;
        for (DataComponentType<?> type : components.keySet().stream().sorted(Comparator.comparing(String::valueOf)).toList()) {
            if (ignoreCustomData && isIgnoredComponent(type)) {
                continue;
            }
            hash = 31 * hash + Objects.hashCode(type);
            hash = 31 * hash + Objects.hashCode(components.get(type));
        }
        return hash;
    }

    public static boolean normalizedComponentsEqual(DataComponentMap left, DataComponentMap right, boolean ignoreCustomData) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        Set<DataComponentType<?>> keys = new HashSet<>();
        keys.addAll(left.keySet());
        keys.addAll(right.keySet());
        for (DataComponentType<?> type : keys) {
            if (ignoreCustomData && isIgnoredComponent(type)) {
                continue;
            }
            if (!Objects.equals(left.get(type), right.get(type))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIgnoredComponent(DataComponentType<?> type) {
        Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
        return id != null && BridgeConfig.current().ignoredComponents().contains(id);
    }

    public static boolean isShulkerBox(ItemStack stack) {
        return isShulkerBoxItem(stack.getItem());
    }

    public static boolean isShulkerBoxItem(Item item) {
        return item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
    }
}
