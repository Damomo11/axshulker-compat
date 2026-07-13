package com.momo.stackbridge.integration;

public interface TargetIntegration {
    String modId();

    default boolean enabledByDefault() {
        return true;
    }
}
