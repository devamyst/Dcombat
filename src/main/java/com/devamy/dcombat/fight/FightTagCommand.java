package com.devamy.dcombat.fight;

import com.devamy.dcombat.config.implementation.MessagesSettings;
import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.fight.event.CancelTagReason;
import com.devamy.dcombat.fight.event.CauseOfTag;
import com.devamy.dcombat.fight.event.CauseOfUnTag;
import com.devamy.dcombat.fight.event.FightTagEvent;
import com.devamy.dcombat.fight.event.FightUntagEvent;
import com.devamy.dcombat.fight.stats.CombatStats;
import com.devamy.dcombat.fight.stats.FightStatsService;
import com.devamy.dcombat.notification.NoticeService;
import com.eternalcode.multification.notice.Notice;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.priority.Priority;
import dev.rollczi.litecommands.annotations.priority.PriorityValue;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "combatlog", aliases = "combat")
public class FightTagCommand {

    private static final Notice PLAYER_STATS_MESSAGE = Notice.chat(
        "<gradient:#ff6666:#ff0000>⚔ <white>{PLAYER}</white> Combat Stats</gradient>\n" +
        "<gray>Tags: <white>{TAGS}</white></gray>\n" +
        "<gray>Kills: <white>{KILLS}</white></gray>\n" +
        "<gray>Deaths: <white>{DEATHS}</white></gray>\n" +
        "<gray>Time in combat: <white>{TIME}</white></gray>"
    );

    private final FightManager fightManager;
    private final NoticeService noticeService;
    private final PluginConfig config;
    private final FightStatsService statsService;

    public FightTagCommand(FightManager fightManager, NoticeService noticeService, PluginConfig config, FightStatsService statsService) {
        this.fightManager = fightManager;
        this.noticeService = noticeService;
        this.config = config;
        this.statsService = statsService;
    }

    @Execute(name = "status")
    @Permission("dcombat.status")
    void status(@Context CommandSender sender, @Arg Player target) {
        UUID targetUniqueId = target.getUniqueId();

        this.noticeService.create()
            .notice(this.fightManager.isInCombat(targetUniqueId)
                ? this.config.messagesSettings.admin.playerInCombat
                : this.config.messagesSettings.admin.playerNotInCombat
            )
            .placeholder("{PLAYER}", target.getName())
            .viewer(sender)
            .send();
    }

    @Execute(name = "tag")
    @Permission("dcombat.tag")
    @Priority(PriorityValue.HIGH)
    void tag(@Context CommandSender sender, @Arg Player target) {
        UUID targetUniqueId = target.getUniqueId();
        Duration time = this.config.settings.combatTimerDuration;

        FightTagEvent event = this.fightManager.tag(targetUniqueId, time, CauseOfTag.COMMAND);

        if (event.isCancelled()) {
            CancelTagReason cancelReason = event.getCancelReason();

            this.tagoutReasonHandler(sender, cancelReason, this.config.messagesSettings);

            return;
        }

        this.noticeService.create()
            .notice(this.config.messagesSettings.admin.adminTagPlayer)
            .placeholder("{PLAYER}", target.getName())
            .viewer(sender)
            .send();
    }

    @Execute(name = "tag")
    @Permission("dcombat.tag")
    void tagMultiple(@Context CommandSender sender, @Arg Player firstTarget, @Arg Player secondTarget) {
        Duration combatTime = this.config.settings.combatTimerDuration;
        MessagesSettings messagesSettings = this.config.messagesSettings;

        if (sender.equals(firstTarget) || sender.equals(secondTarget)) {

            this.noticeService.create()
                .notice(messagesSettings.admin.adminCannotTagSelf)
                .viewer(sender)
                .send();

            return;
        }

        UUID firstId = firstTarget.getUniqueId();
        UUID secondId = secondTarget.getUniqueId();

        FightTagEvent firstTagEvent = this.fightManager.tag(firstId, combatTime, CauseOfTag.COMMAND, secondId);
        FightTagEvent secondTagEvent = this.fightManager.tag(secondId, combatTime, CauseOfTag.COMMAND, firstId);

        boolean firstCancelled = firstTagEvent.isCancelled();
        boolean secondCancelled = secondTagEvent.isCancelled();

        if (!firstCancelled && !secondCancelled) {
            this.noticeService.create()
                .notice(messagesSettings.admin.adminTagMultiplePlayers)
                .placeholder("{FIRST_PLAYER}", firstTarget.getName())
                .placeholder("{SECOND_PLAYER}", secondTarget.getName())
                .viewer(sender)
                .send();
            return;
        }

        if (firstCancelled) {
            this.tagoutReasonHandler(sender, firstTagEvent.getCancelReason(), messagesSettings);
            if (!secondCancelled) {
                this.fightManager.untag(secondId, CauseOfUnTag.COMMAND);
            }
        }

        if (secondCancelled) {
            this.tagoutReasonHandler(sender, secondTagEvent.getCancelReason(), messagesSettings);
            if (!firstCancelled) {
                this.fightManager.untag(firstId, CauseOfUnTag.COMMAND);
            }
        }

        if (firstCancelled || secondCancelled) {
            this.noticeService.create()
                .notice(messagesSettings.admin.adminTagTry)
                .placeholder("{FIRST_PLAYER}", firstTarget.getName())
                .placeholder("{SECOND_PLAYER}", secondTarget.getName())
                .viewer(sender)
                .send();
        }
    }

    @Execute(name = "untag")
    @Permission("dcombat.untag")
    void untag(@Context CommandSender sender, @Arg Player target) {
        UUID targetUniqueId = target.getUniqueId();

        if (!this.fightManager.isInCombat(targetUniqueId)) {
            this.noticeService.create()
                .notice(this.config.messagesSettings.admin.adminPlayerNotInCombat)
                .placeholder("{PLAYER}", target.getName())
                .viewer(sender)
                .send();
            return;
        }

        FightUntagEvent event = this.fightManager.untag(targetUniqueId, CauseOfUnTag.COMMAND);
        if (event.isCancelled()) {
            return;
        }

        this.noticeService.create()
            .notice(this.config.messagesSettings.admin.adminUntagPlayer)
            .placeholder("{PLAYER}", target.getName())
            .viewer(sender)
            .send();
    }

    @Execute(name = "untagall")
    @Permission("dcombat.untagall")
    void untagAll(@Context CommandSender sender) {
        int combatPlayersSize = this.fightManager.getFights().size();

        this.fightManager.getFights().stream()
            .map(FightTag::getTaggedPlayer)
            .collect(Collectors.toSet())
            .forEach(uuid -> this.fightManager.untag(uuid, CauseOfUnTag.COMMAND));

        this.noticeService.create()
            .notice(this.config.messagesSettings.admin.adminUntagAll)
            .placeholder("{COUNT}", String.valueOf(combatPlayersSize))
            .viewer(sender)
            .send();
    }

    @Execute(name = "stats")
    @Permission("dcombat.stats")
    void stats(@Context CommandSender sender) {
        long activeCombatPlayers = this.fightManager.getFights().stream()
            .filter(fightTag -> !fightTag.isExpired())
            .count();

        this.noticeService.create()
            .notice(this.config.messagesSettings.admin.combatStats)
            .placeholder("{COUNT}", String.valueOf(activeCombatPlayers))
            .viewer(sender)
            .send();
    }

    @Execute(name = "stats")
    @Permission("dcombat.stats.other")
    void statsOther(@Context CommandSender sender, @Arg Player target) {
        CombatStats stats = this.statsService.getStats(target.getUniqueId());

        this.noticeService.create()
            .notice(PLAYER_STATS_MESSAGE)
            .placeholder("{PLAYER}", target.getName())
            .placeholder("{TAGS}", String.valueOf(stats.getTotalTags()))
            .placeholder("{KILLS}", String.valueOf(stats.getCombatKills()))
            .placeholder("{DEATHS}", String.valueOf(stats.getCombatDeaths()))
            .placeholder("{TIME}", this.formatDuration(Duration.ofMillis(stats.getTotalCombatTimeMillis())))
            .viewer(sender)
            .send();
    }

    private String formatDuration(Duration duration) {
        long totalSeconds = duration.getSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    private void tagoutReasonHandler(
        CommandSender sender, CancelTagReason cancelReason,
        MessagesSettings messagesSettings) {
        if (cancelReason == CancelTagReason.TAGOUT) {
            this.noticeService.create()
                .notice(messagesSettings.admin.adminTagOutCanceled)
                .viewer(sender)
                .send();
        }
    }
}
