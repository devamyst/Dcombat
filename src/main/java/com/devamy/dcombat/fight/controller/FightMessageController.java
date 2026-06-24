package com.devamy.dcombat.fight.controller;

import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.fight.event.FightTagEvent;
import com.devamy.dcombat.fight.event.FightUntagEvent;
import com.devamy.dcombat.notification.NoticeService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FightMessageController implements Listener {

    private final FightManager fightManager;
    private final NoticeService noticeService;
    private final PluginConfig config;
    private final Server server;

    public FightMessageController(FightManager fightManager, NoticeService noticeService, PluginConfig config, Server server) {
        this.fightManager = fightManager;
        this.noticeService = noticeService;
        this.config = config;
        this.server = server;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onTag(FightTagEvent event) {
        Player player = this.server.getPlayer(event.getPlayer());

        if (player == null) {
            return;
        }

        if (this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        this.noticeService.create()
            .player(player.getUniqueId())
            .notice(this.config.messagesSettings.playerTagged)
            .send();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onUnTag(FightUntagEvent event) {
        Player player = this.server.getPlayer(event.getPlayer());

        if (player == null) {
            return;
        }

        this.noticeService.create()
            .player(player.getUniqueId())
            .notice(this.config.messagesSettings.playerUntagged)
            .send();

    }
}
