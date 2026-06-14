package com.devamy.dcombat.config.implementation;

import com.devamy.dcombat.WhitelistBlacklistMode;
import eu.okaeri.configs.OkaeriConfig;
import java.util.List;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CombatSettings extends OkaeriConfig {
    public boolean releaseAttackerOnVictimDeath = true;

    public boolean disableElytraUsage = true;

    public boolean disableElytraOnDamage = true;

    public boolean disableFlying = true;

    public boolean unequipElytraOnCombat = true;

    public boolean disableFireworks = true;

    public boolean enableDamageCauseLogging = false;

    public WhitelistBlacklistMode damageCauseRestrictionMode = WhitelistBlacklistMode.WHITELIST;

    public List<DamageCause> loggedDamageCauses = List.of(
        DamageCause.LAVA,
        DamageCause.CONTACT,
        DamageCause.FIRE,
        DamageCause.FIRE_TICK
    );

    public List<EntityType> ignoredProjectileTypes = List.of(
        EntityType.ENDER_PEARL,
        EntityType.EGG
    );

    public EventPriority quitPunishmentEventPriority = EventPriority.NORMAL;

    public List<String> whitelistedKickReasons = List.of("Kicked for inactivity", "Timed out", "Server is restarting");
}
