package com.devamy.dcombat.scheduler;

import java.time.Duration;
import org.bukkit.Location;

public interface Scheduler {

    void runAsync(Runnable runnable);

    void runLater(Runnable runnable, Duration delay);

    void runLater(Location location, Runnable runnable, Duration delay);

    void runLaterAsync(Runnable runnable, Duration delay);

    void timer(Runnable runnable, Duration delay, Duration period);

    void timerAsync(Runnable runnable, Duration delay, Duration period);
}
