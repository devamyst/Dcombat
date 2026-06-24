package com.devamy.dcombat.updater;

import com.devamy.dcombat.config.implementation.PluginConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdaterNotificationController implements Listener {

    private static final String NEW_VERSION_AVAILABLE = "<b><gradient:#8a1212:#fc6b03>Dcombat:</gradient></b> <color:#fce303>New version <white>{VERSION}</white> is available, please update!";

    private static final String CURRENT_VERSION_STRING = "<b><gradient:#8a1212:#fc6b03>Dcombat:</gradient></b> <color:#fce303>You are running the latest version!";

    private final UpdaterService updaterService;
    private final PluginConfig pluginConfig;
    private final MiniMessage miniMessage;

    public UpdaterNotificationController(UpdaterService updaterService, PluginConfig pluginConfig, MiniMessage miniMessage) {
        this.updaterService = updaterService;
        this.pluginConfig = pluginConfig;
        this.miniMessage = miniMessage;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!shouldNotify(player)) {
            return;
        }

        this.updaterService.checkForUpdate()
            .thenAccept(result -> {
                if (result.isUpdateAvailable()) {
                    String msg = result.getLatestVersion()
                        .map(version -> NEW_VERSION_AVAILABLE.replace("{VERSION}", version))
                        .orElse(NEW_VERSION_AVAILABLE.replace("{VERSION}", "unknown"));
                    player.sendMessage(this.miniMessage.deserialize(msg));
                }
            })
            .exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });
    }

    private boolean shouldNotify(Player player) {
        return player.hasPermission("dcombat.receiveupdates") && this.pluginConfig.settings.notifyAboutUpdates;
    }

}
