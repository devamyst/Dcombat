package com.devamy.dcombat.border;

import com.devamy.dcombat.border.animation.block.BlockSettings;
import com.devamy.dcombat.border.animation.particle.ParticleSettings;
import eu.okaeri.configs.OkaeriConfig;
import java.time.Duration;
import org.jetbrains.annotations.ApiStatus;

public class BorderSettings extends OkaeriConfig {

        public double distance = 6.5;

        public BlockSettings block = new BlockSettings();

        public ParticleSettings particle = new ParticleSettings();

    @ApiStatus.Internal
    public Duration indexRefreshDelay() {
        return Duration.ofSeconds(1);
    }

    public int distanceRounded() {
        return (int) Math.ceil(this.distance);
    }

    public boolean isEnabled() {
        return this.block.enabled || this.particle.enabled;
    }

}
