package com.momo.stackbridge;

import net.fabricmc.api.ClientModInitializer;

import com.momo.stackbridge.config.BridgeConfig;

public final class AxShulkerCompatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BridgeConfig.reload();
        AxShulkerCompat.LOGGER.info("AxShulker Compat client hooks ready.");
    }
}
