package com.devamy.dcombat;

import com.devamy.dcombat.scheduler.BukkitScheduler;
import com.devamy.dcombat.scheduler.FoliaScheduler;
import com.devamy.dcombat.scheduler.Scheduler;
import org.bukkit.plugin.Plugin;

public final class CombatSchedulerAdapter {

    private static final String FOLIA_CLASS = "io.papermc.paper.threadedregions.RegionizedServer";

    private CombatSchedulerAdapter() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }

    public static boolean isFolia() {
        try {
            Class.forName(FOLIA_CLASS);
            return true;
        }
        catch (ClassNotFoundException exception) {
            return false;
        }
    }

    public static Scheduler getAdaptiveScheduler(Plugin plugin, boolean folia) {
        return folia ? new FoliaScheduler(plugin) : new BukkitScheduler(plugin);
    }
}
