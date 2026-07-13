package com.momo.stackbridge.identity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

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

    public static int normalizedComponentsHash(ComponentMap components, boolean ignoreCustomData) {
        if (components == null) {
            return 0;
        }
        int hash = 1;
        for (ComponentType<?> type : components.getTypes().stream().sorted(Comparator.comparing(String::valueOf)).toList()) {
            if (ignoreCustomData && isIgnoredComponent(type)) {
                continue;
            }
            hash = 31 * hash + Objects.hashCode(type);
            hash = 31 * hash + Objects.hashCode(components.get(type));
        }
        return hash;
    }

    public static boolean normalizedComponentsEqual(ComponentMap left, ComponentMap right, boolean ignoreCustomData) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        Set<ComponentType<?>> keys = new HashSet<>();
        keys.addAll(left.getTypes());
        keys.addAll(right.getTypes());
        for (ComponentType<?> type : keys) {
            if (ignoreCustomData && isIgnoredComponent(type)) {
                continue;
            }
            if (!Objects.equals(left.get(type), right.get(type))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIgnoredComponent(ComponentType<?> type) {
        Identifier id = Registries.DATA_COMPONENT_TYPE.getId(type);
        return id != null && BridgeConfig.current().ignoredComponents().contains(id);
    }

    public static boolean isShulkerBox(ItemStack stack) {
        return isShulkerBoxItem(stack.getItem());
    }

    public static boolean isShulkerBoxItem(Item item) {
        return item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
    }
}
