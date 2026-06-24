package com.devamy.dcombat.fight;

import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.fight.bossbar.BossBarController;
import com.devamy.dcombat.fight.event.CauseOfUnTag;
import com.devamy.dcombat.fight.stats.FightStatsService;
import com.devamy.dcombat.notification.NoticeService;
import com.devamy.dcombat.util.DurationUtil;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class FightTask implements Runnable {

    private final Server server;
    private final PluginConfig config;
    private final FightManager fightManager;
    private final NoticeService noticeService;
    private final FightStatsService statsService;
    private final BossBarController bossBarController;

    public FightTask(Server server, PluginConfig config, FightManager fightManager, NoticeService noticeService, FightStatsService statsService, BossBarController bossBarController) {
        this.server = server;
        this.config = config;
        this.fightManager = fightManager;
        this.noticeService = noticeService;
        this.statsService = statsService;
        this.bossBarController = bossBarController;
    }

    @Override
    public void run() {
        for (FightTag fightTag : this.fightManager.getFights()) {
            Player player = this.server.getPlayer(fightTag.getTaggedPlayer());

            if (player == null) {
                continue;
            }

            UUID playerUniqueId = player.getUniqueId();

            if (fightTag.isExpired()) {
                this.fightManager.untag(playerUniqueId, CauseOfUnTag.TIME_EXPIRED);
                continue;
            }

            Duration remaining = fightTag.getRemainingDuration();

            String opponent = Optional.ofNullable(fightTag.getTagger())
                .filter(uuid -> !uuid.equals(playerUniqueId))
                .map(this.server::getPlayer)
                .map(Player::getName)
                .orElse(this.config.messagesSettings.unknownPlayerPlaceholder);

            this.noticeService.create()
                .player(player.getUniqueId())
                .notice(this.config.messagesSettings.combatNotification)
                .placeholder("{TIME}", DurationUtil.format(remaining, this.config.messagesSettings.withoutMillis))
                .placeholder("{OPPONENT}", opponent)
                .send();

            this.statsService.addCombatTime(playerUniqueId, Duration.ofSeconds(1));
        }

        this.bossBarController.updateBossBars();
    }
}
