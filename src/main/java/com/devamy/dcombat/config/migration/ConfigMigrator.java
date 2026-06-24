package com.devamy.dcombat.config.migration;

import com.devamy.dcombat.config.implementation.PluginConfig;
import java.util.logging.Logger;

public class ConfigMigrator {

    private static final int CURRENT_VERSION = 1;

    private final Logger logger;

    public ConfigMigrator(Logger logger) {
        this.logger = logger;
    }

    public void migrate(PluginConfig config) {
        int version = config.configVersion;

        if (version > CURRENT_VERSION) {
            this.logger.warning("Config version (" + version + ") is newer than plugin version (" + CURRENT_VERSION + "). Downgrading may cause issues.");
            return;
        }

        if (version < CURRENT_VERSION) {
            this.logger.info("Migrating config from version " + version + " to " + CURRENT_VERSION + "...");
            this.applyMigrations(config, version);
            config.configVersion = CURRENT_VERSION;
            config.save();
            this.logger.info("Config migration completed successfully.");
        }
    }

    private void applyMigrations(PluginConfig config, int fromVersion) {
        if (fromVersion < 1) {
            this.migrateToV1(config);
        }
    }

    private void migrateToV1(PluginConfig config) {
        // Initial version - nothing to migrate
    }
}
