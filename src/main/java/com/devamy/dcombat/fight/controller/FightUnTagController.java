package com.devamy.dcombat.fight.controller;

import com.devamy.dcombat.crystalpvp.CrystalPvpConstants;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.fight.event.CauseOfUnTag;
import com.devamy.dcombat.fight.stats.FightStatsService;
import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.fight.logout.LogoutService;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FightUnTagController implements Listener {

    private final FightManager fightManager;
    private final PluginConfig config;
    private final LogoutService logoutService;
    private final FightStatsService statsService;

    public FightUnTagController(FightManager fightManager, PluginConfig config, LogoutService logoutService, FightStatsService statsService) {
        this.fightManager = fightManager;
        this.config = config;
        this.logoutService = logoutService;
        this.statsService = statsService;
    }

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        UUID playerUniqueId = player.getUniqueId();
        Optional<UUID> optionalKiller;

        if (killer != null) {
            optionalKiller = Optional.of(killer.getUniqueId());
        } else {
            optionalKiller = this.getCrystalKiller(player);
        }

        if (!this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        CauseOfUnTag cause = this.getDeathCause(playerUniqueId, optionalKiller.orElse(null));

        this.statsService.addDeath(playerUniqueId);
        if (optionalKiller.isPresent()) {
            this.statsService.addKill(optionalKiller.get());
        }
        this.statsService.addCombatTime(playerUniqueId, Duration.ZERO);

        this.fightManager.untag(player.getUniqueId(), cause);

        if (optionalKiller.isPresent() && this.config.combat.releaseAttackerOnVictimDeath) {
            this.fightManager.untag(optionalKiller.get(), CauseOfUnTag.ATTACKER_RELEASE);
        }
    }

    private CauseOfUnTag getDeathCause(UUID playerUniqueId, UUID killerUniqueId) {
        if (this.logoutService.hasLoggedOut(playerUniqueId)) {
            return CauseOfUnTag.LOGOUT;
        }

        if (killerUniqueId == null) {
            return CauseOfUnTag.DEATH;
        }

        if (this.fightManager.isInCombat(killerUniqueId)) {
            return CauseOfUnTag.DEATH_BY_PLAYER;
        }

        return CauseOfUnTag.DEATH;
    }

    private Optional<UUID> getCrystalKiller(Player player) {
        EntityDamageEvent lastDamageCause = player.getLastDamageCause();

        if (lastDamageCause instanceof EntityDamageByBlockEvent damageByBlockEvent) {
            return CrystalPvpConstants.getDamagerUniqueIdFromRespawnAnchor(damageByBlockEvent);
        }

        if (lastDamageCause instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            return CrystalPvpConstants.getDamagerUniqueIdFromEndCrystal(damageByEntityEvent);
        }

        return Optional.empty();
    }
}
