package com.devamy.dcombat;

import com.devamy.dcombat.config.ConfigService;
import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.notification.NoticeService;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.time.Duration;
import org.bukkit.command.CommandSender;

@Command(name = "combatlog", aliases = "combat")
public class DcombatReloadCommand {

    private final ConfigService configService;
    private final NoticeService noticeService;
    private final PluginConfig config;

    public DcombatReloadCommand(ConfigService configService, NoticeService noticeService, PluginConfig config) {
        this.configService = configService;
        this.noticeService = noticeService;
        this.config = config;
    }

    @Async
    @Execute(name = "reload")
    @Permission("dcombat.reload")
    void execute(@Context CommandSender sender) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        this.configService.reload();

        Duration elapsed = stopwatch.elapsed();
        this.noticeService.create()
            .viewer(sender)
            .notice(this.config.messagesSettings.admin.pluginReloaded)
            .placeholder("{TIME}", String.valueOf(elapsed.toMillis()))
            .send();

    }
}
