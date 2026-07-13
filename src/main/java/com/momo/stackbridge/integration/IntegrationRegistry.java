package com.momo.stackbridge.integration;

import java.util.List;

public final class IntegrationRegistry {
    private IntegrationRegistry() {
    }

    public static List<TargetIntegration> all() {
        return List.of(
            new ItemScrollerIntegration(),
            new InventoryProfilesNextIntegration()
        );
    }

    public static final class ItemScrollerIntegration implements TargetIntegration {
        @Override
        public String modId() {
            return "itemscroller";
        }
    }

    public static final class InventoryProfilesNextIntegration implements TargetIntegration {
        @Override
        public String modId() {
            return "inventoryprofilesnext";
        }
    }
}
