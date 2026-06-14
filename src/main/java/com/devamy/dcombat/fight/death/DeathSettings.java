package com.devamy.dcombat.fight.death;

import com.cryptomorin.xseries.particles.XParticle;
import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.FireworkEffect;

public class DeathSettings extends OkaeriConfig {

    public LightningSettings lightning = new LightningSettings();

    public static class LightningSettings extends OkaeriConfig {
        public boolean afterEveryDeath = false;

        public boolean inCombat = true;
    }

    public FlareSettings firework = new FlareSettings();

    public static class FlareSettings extends OkaeriConfig {
        public boolean afterEveryDeath = false;

        public boolean inCombat = false;

        public int power = 2;

        public FireworkEffect.Type fireworkType = FireworkEffect.Type.BALL;

        public String primaryColor = "#a80022";

        public String fadeColor = "#0a0a0a";

        public boolean particlesEnabled = true;

        public XParticle mainParticle = XParticle.CAMPFIRE_COSY_SMOKE;

        public int mainParticleCount = 3;

        public XParticle secondaryParticle = XParticle.SMALL_FLAME;

        public int secondaryParticleCount = 3;
    }
}
