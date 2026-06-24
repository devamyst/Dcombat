package com.devamy.dcombat.fight.stats;

import com.devamy.dcombat.notification.NoticeService;
import com.eternalcode.multification.notice.Notice;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.time.Duration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "combatlog", aliases = "combat")
public class CombatStatsCommand {

    private static final Notice STATS_MESSAGE = Notice.chat(
        "<gradient:#ff6666:#ff0000>⚔ <white>{PLAYER}</white> Combat Stats</gradient>\n" +
        "<gray>Tags: <white>{TAGS}</white></gray>\n" +
        "<gray>Kills: <white>{KILLS}</white></gray>\n" +
        "<gray>Deaths: <white>{DEATHS}</white></gray>\n" +
        "<gray>Time in combat: <white>{TIME}</white></gray>"
    );

    private static final Notice SELF_STATS = Notice.chat(
        "<gradient:#ff6666:#ff0000>⚔ Your Combat Stats</gradient>\n" +
        "<gray>Tags: <white>{TAGS}</white></gray>\n" +
        "<gray>Kills: <white>{KILLS}</white></gray>\n" +
        "<gray>Deaths: <white>{DEATHS}</white></gray>\n" +
        "<gray>Time in combat: <white>{TIME}</white></gray>"
    );

    private final FightStatsService statsService;
    private final NoticeService noticeService;

    public CombatStatsCommand(FightStatsService statsService, NoticeService noticeService) {
        this.statsService = statsService;
        this.noticeService = noticeService;
    }

    @Execute(name = "stats")
    @Permission("dcombat.stats.self")
    void statsSelf(@Context Player sender) {
        CombatStats stats = this.statsService.getStats(sender.getUniqueId());
        this.sendStats(sender, SELF_STATS, sender.getName(), stats);
    }

    @Execute(name = "stats")
    @Permission("dcombat.stats.other")
    void statsOther(@Context CommandSender sender, @Arg Player target) {
        CombatStats stats = this.statsService.getStats(target.getUniqueId());
        this.sendStats(sender, STATS_MESSAGE, target.getName(), stats);
    }

    private void sendStats(CommandSender sender, Notice message, String playerName, CombatStats stats) {
        this.noticeService.create()
            .notice(message)
            .placeholder("{PLAYER}", playerName)
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
        }
        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        }
        return String.format("%ds", seconds);
    }
}
