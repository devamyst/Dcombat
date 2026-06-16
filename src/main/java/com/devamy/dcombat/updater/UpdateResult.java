package com.devamy.dcombat.updater;

public record UpdateResult(boolean updateAvailable) {

    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }
}
