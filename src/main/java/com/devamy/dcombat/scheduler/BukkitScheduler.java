package com.devamy.dcombat.scheduler;

import java.time.Duration;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public final class BukkitScheduler implements Scheduler {

    private final Plugin plugin;

    public BukkitScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runAsync(Runnable runnable) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    @Override
    public void runLater(Runnable runnable, Duration delay) {
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, runnable, toTicks(delay));
    }

    @Override
    public void runLater(Location location, Runnable runnable, Duration delay) {
        this.runLater(runnable, delay);
    }

    @Override
    public void runLaterAsync(Runnable runnable, Duration delay) {
        this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, toTicks(delay));
    }

    @Override
    public void timer(Runnable runnable, Duration delay, Duration period) {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, runnable, toTicks(delay), toTicks(period));
    }

    @Override
    public void timerAsync(Runnable runnable, Duration delay, Duration period) {
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, toTicks(delay), toTicks(period));
    }

    static long toTicks(Duration duration) {
        return Math.max(1L, duration.toMillis() / 50L);
    }
}
