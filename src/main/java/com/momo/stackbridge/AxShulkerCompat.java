package com.momo.stackbridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

import com.momo.stackbridge.config.BridgeConfig;

public final class AxShulkerCompat implements ModInitializer {
    public static final String MOD_ID = "axshulker-compat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        BridgeConfig.load();
        LOGGER.info("AxShulker Compat loaded.");
    }
}
