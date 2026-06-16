package com.devamy.dcombat;

import com.devamy.dcombat.scheduler.BukkitScheduler;
import com.devamy.dcombat.scheduler.FoliaScheduler;
import com.devamy.dcombat.scheduler.Scheduler;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

public final class CombatSchedulerAdapter {

    private static final String FOLIA_CLASS = "io.papermc.paper.threadedregions.RegionizedServer";

    private CombatSchedulerAdapter() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }

    public static Scheduler getAdaptiveScheduler(Plugin plugin) {
        Logger logger = plugin.getLogger();

        try {
            Class.forName(FOLIA_CLASS);
            logger.info("» Detected Folia environment. Using FoliaScheduler.");
            return new FoliaScheduler(plugin);
        }
        catch (ClassNotFoundException exception) {
            logger.info("» Detected Bukkit/Paper environment. Using BukkitScheduler.");
            return new BukkitScheduler(plugin);
        }
    }
}
