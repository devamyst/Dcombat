package com.devamy.dcombat.fight.logout;

import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.fight.event.FightUntagEvent;
import com.devamy.dcombat.notification.NoticeService;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class LogoutAlertController implements Listener {

    private static final String NOTIFY_PERMISSION = "dcombat.notify";

    private final FightManager fightManager;
    private final Server server;
    private final NoticeService noticeService;
    private final PluginConfig config;

    public LogoutAlertController(FightManager fightManager, Server server, NoticeService noticeService, PluginConfig config) {
        this.fightManager = fightManager;
        this.server = server;
        this.noticeService = noticeService;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onLogout(FightUntagEvent event) {
        if (event.getCause() != com.devamy.dcombat.fight.event.CauseOfUnTag.LOGOUT) {
            return;
        }

        UUID playerId = event.getPlayer();
        Player player = this.server.getPlayer(playerId);

        String playerName = player != null ? player.getName() : playerId.toString();

        for (Player online : this.server.getOnlinePlayers()) {
            if (online.hasPermission(NOTIFY_PERMISSION)) {
                this.noticeService.create()
                    .viewer(online)
                    .notice(this.config.messagesSettings.playerLoggedOutDuringCombat)
                    .placeholder("{PLAYER}", playerName)
                    .send();
            }
        }
    }
}
