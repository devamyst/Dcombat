package com.devamy.dcombat;

import com.devamy.dcombat.config.ConfigService;
import com.devamy.dcombat.notification.NoticeService;
import com.eternalcode.multification.notice.Notice;
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

    private static final Notice RELOAD_MESSAGE = Notice.builder()
        .chat("<b><gradient:#8a1212:#fc6b03>Dcombat:</gradient></b> Reloaded Dcombat in <color:#fce303>{TIME}ms!</color>")
        .build();

    private final ConfigService configService;
    private final NoticeService noticeService;

    public DcombatReloadCommand(ConfigService configService, NoticeService noticeService) {
        this.configService = configService;
        this.noticeService = noticeService;
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
            .notice(RELOAD_MESSAGE)
            .placeholder("{TIME}", String.valueOf(elapsed.toMillis()))
            .send();

    }
}
