package com.devamy.dcombat.fight.stats;

import java.util.UUID;

public class CombatStats {

    private final UUID playerId;
    private int totalTags;
    private int combatKills;
    private int combatDeaths;
    private long totalCombatTimeMillis;

    public CombatStats(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public int getTotalTags() {
        return this.totalTags;
    }

    public void incrementTags() {
        this.totalTags++;
    }

    public int getCombatKills() {
        return this.combatKills;
    }

    public void incrementKills() {
        this.combatKills++;
    }

    public int getCombatDeaths() {
        return this.combatDeaths;
    }

    public void incrementDeaths() {
        this.combatDeaths++;
    }

    public long getTotalCombatTimeMillis() {
        return this.totalCombatTimeMillis;
    }

    public void addCombatTime(long millis) {
        this.totalCombatTimeMillis += millis;
    }
}
