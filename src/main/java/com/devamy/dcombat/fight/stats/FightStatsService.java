package com.devamy.dcombat.fight.stats;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FightStatsService {

    private final Map<UUID, CombatStats> statsMap = new ConcurrentHashMap<>();

    public CombatStats getOrCreate(UUID playerId) {
        return this.statsMap.computeIfAbsent(playerId, CombatStats::new);
    }

    public CombatStats getStats(UUID playerId) {
        return this.statsMap.computeIfAbsent(playerId, CombatStats::new);
    }

    public void addTag(UUID playerId) {
        this.getOrCreate(playerId).incrementTags();
    }

    public void addKill(UUID playerId) {
        this.getOrCreate(playerId).incrementKills();
    }

    public void addDeath(UUID playerId) {
        this.getOrCreate(playerId).incrementDeaths();
    }

    public void addCombatTime(UUID playerId, Duration duration) {
        this.getOrCreate(playerId).addCombatTime(duration.toMillis());
    }

    public Map<UUID, CombatStats> getAll() {
        return this.statsMap;
    }
}
