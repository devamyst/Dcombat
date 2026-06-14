package com.devamy.dcombat.fight.effect;

import com.cryptomorin.xseries.XPotion;
import eu.okaeri.configs.OkaeriConfig;
import java.util.Map;
import org.bukkit.potion.PotionEffectType;

public class FightEffectSettings extends OkaeriConfig {

    public boolean customEffectsEnabled = false;

    public Map<PotionEffectType, Integer> customEffects = Map.of(
        requirePotionEffectType(XPotion.SPEED), 1,
        requirePotionEffectType(XPotion.RESISTANCE), 0
    );

    private static PotionEffectType requirePotionEffectType(XPotion potion) {
        PotionEffectType potionEffectType = potion.get();
        if (potionEffectType == null) {
            throw new IllegalStateException("Potion effect " + potion.name() + " is not supported by this server version");
        }

        return potionEffectType;
    }
}
