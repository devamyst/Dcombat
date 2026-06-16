package com.devamy.dcombat.scheduler;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public final class FoliaScheduler implements Scheduler {

    private final Plugin plugin;

    public FoliaScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runAsync(Runnable runnable) {
        this.invokeAsync("runNow", new Class<?>[] { Plugin.class, Consumer.class }, this.plugin, (Consumer<Object>) ignored -> runnable.run());
    }

    @Override
    public void runLater(Runnable runnable, Duration delay) {
        this.invokeGlobal("runDelayed", new Class<?>[] { Plugin.class, Consumer.class, long.class }, this.plugin, (Consumer<Object>) ignored -> runnable.run(), BukkitScheduler.toTicks(delay));
    }

    @Override
    public void runLater(Location location, Runnable runnable, Duration delay) {
        try {
            Object scheduler = Bukkit.class.getMethod("getRegionScheduler").invoke(null);
            Method method = scheduler.getClass().getMethod(
                "runDelayed",
                Plugin.class,
                org.bukkit.World.class,
                int.class,
                int.class,
                Consumer.class,
                long.class
            );
            method.invoke(
                scheduler,
                this.plugin,
                location.getWorld(),
                location.getBlockX() >> 4,
                location.getBlockZ() >> 4,
                (Consumer<Object>) ignored -> runnable.run(),
                BukkitScheduler.toTicks(delay)
            );
        }
        catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to schedule region task on Folia", exception);
        }
    }

    @Override
    public void runLaterAsync(Runnable runnable, Duration delay) {
        this.invokeAsync(
            "runDelayed",
            new Class<?>[] { Plugin.class, Consumer.class, long.class, TimeUnit.class },
            this.plugin,
            (Consumer<Object>) ignored -> runnable.run(),
            Math.max(1L, delay.toMillis()),
            TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void timer(Runnable runnable, Duration delay, Duration period) {
        this.invokeGlobal(
            "runAtFixedRate",
            new Class<?>[] { Plugin.class, Consumer.class, long.class, long.class },
            this.plugin,
            (Consumer<Object>) ignored -> runnable.run(),
            BukkitScheduler.toTicks(delay),
            BukkitScheduler.toTicks(period)
        );
    }

    @Override
    public void timerAsync(Runnable runnable, Duration delay, Duration period) {
        this.invokeAsync(
            "runAtFixedRate",
            new Class<?>[] { Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class },
            this.plugin,
            (Consumer<Object>) ignored -> runnable.run(),
            Math.max(1L, delay.toMillis()),
            Math.max(1L, period.toMillis()),
            TimeUnit.MILLISECONDS
        );
    }

    private void invokeGlobal(String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Object scheduler = Bukkit.class.getMethod("getGlobalRegionScheduler").invoke(null);
            scheduler.getClass().getMethod(methodName, parameterTypes).invoke(scheduler, args);
        }
        catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to schedule global Folia task", exception);
        }
    }

    private void invokeAsync(String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Object scheduler = Bukkit.class.getMethod("getAsyncScheduler").invoke(null);
            scheduler.getClass().getMethod(methodName, parameterTypes).invoke(scheduler, args);
        }
        catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to schedule async Folia task", exception);
        }
    }
}
