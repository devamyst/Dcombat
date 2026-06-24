package com.devamy.dcombat.updater;

import java.util.Optional;

public class UpdateResult {

    private final boolean updateAvailable;
    private final String latestVersion;

    public UpdateResult(boolean updateAvailable) {
        this(updateAvailable, null);
    }

    public UpdateResult(boolean updateAvailable, String latestVersion) {
        this.updateAvailable = updateAvailable;
        this.latestVersion = latestVersion;
    }

    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }

    public Optional<String> getLatestVersion() {
        return Optional.ofNullable(this.latestVersion);
    }
}
