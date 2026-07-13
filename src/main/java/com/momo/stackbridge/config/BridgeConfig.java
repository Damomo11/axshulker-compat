package com.momo.stackbridge.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import com.momo.stackbridge.AxShulkerCompat;

public final class BridgeConfig {
    private static final String FILE_NAME = "ignored-components.txt";
    private static final Identifier CUSTOM_DATA = Identifier.of("minecraft", "custom_data");
    private static volatile BridgeConfig instance = new BridgeConfig(true, Set.of(CUSTOM_DATA));

    private final boolean enabled;
    private final Set<Identifier> ignoredComponents;

    public BridgeConfig(boolean enabled, Set<Identifier> ignoredComponents) {
        this.enabled = enabled;
        this.ignoredComponents = Set.copyOf(ignoredComponents);
    }

    public boolean enabled() {
        return enabled;
    }

    public Set<Identifier> ignoredComponents() {
        return ignoredComponents;
    }

    public static BridgeConfig current() {
        return instance;
    }

    public static void load() {
        instance = readOrCreate();
    }

    public static void reload() {
        instance = readOrCreate();
    }

    private static BridgeConfig readOrCreate() {
        Path dir = FabricLoader.getInstance().getConfigDir().resolve(AxShulkerCompat.MOD_ID);
        Path file = dir.resolve(FILE_NAME);

        try {
            Files.createDirectories(dir);
            if (Files.notExists(file)) {
                Files.writeString(file, defaultTemplate(), StandardCharsets.UTF_8);
                return new BridgeConfig(true, Set.of(CUSTOM_DATA));
            }
            return parse(file);
        } catch (IOException e) {
            AxShulkerCompat.LOGGER.warn("Failed to load config, using defaults.", e);
            return new BridgeConfig(true, Set.of());
        }
    }

    private static BridgeConfig parse(Path file) throws IOException {
        boolean enabled = true;
        Set<Identifier> ignored = new LinkedHashSet<>();
        ignored.add(CUSTOM_DATA);

        for (String rawLine : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            String line = rawLine.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            if (line.equalsIgnoreCase("enabled=false")) {
                enabled = false;
                continue;
            }
            Identifier id = Identifier.tryParse(line);
            if (id != null) {
                ignored.add(id);
            }
        }

        return new BridgeConfig(enabled, ignored);
    }

    private static String defaultTemplate() {
        return """
                # AxShulker Compat config
                # One entry per line.
                # Put ignored data component ids here later, for example:
                minecraft:custom_data
                #
                # enabled=false
                """;
    }
}
