package com.devamy.dcombat.fight.trident;

import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.util.Delay;

import java.time.Duration;
import java.util.UUID;

public class TridentServiceImpl implements TridentService {

    private final Delay<UUID> delay;

    public TridentServiceImpl(PluginConfig pluginConfig) {
        this.delay = Delay.withDefault(() -> pluginConfig.trident.tridentRiptideDelay);
    }


    @Override
    public void markDelay(UUID uuid) {
        this.delay.markDelay(uuid);
    }

    @Override
    public boolean hasDelay(UUID uuid) {
        return this.delay.hasDelay(uuid);
    }

    @Override
    public void removeDelay(UUID playerId) {
        this.delay.unmarkDelay(playerId);
    }

    @Override
    public Duration getRemainingDelay(UUID uuid) {
        return this.delay.getRemaining(uuid);
    }
}
