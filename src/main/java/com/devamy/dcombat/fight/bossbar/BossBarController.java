package com.devamy.dcombat.fight.bossbar;

import com.devamy.dcombat.config.implementation.BossBarSettings;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.fight.FightTag;
import com.devamy.dcombat.fight.event.FightTagEvent;
import com.devamy.dcombat.fight.event.FightUntagEvent;
import com.devamy.dcombat.util.DurationUtil;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BossBarController implements Listener {

    private final FightManager fightManager;
    private final BossBarSettings settings;
    private final Server server;
    private final MiniMessage miniMessage;
    private final Map<UUID, BossBar> bossBars = new ConcurrentHashMap<>();

    public BossBarController(FightManager fightManager, BossBarSettings settings, Server server, MiniMessage miniMessage) {
        this.fightManager = fightManager;
        this.settings = settings;
        this.server = server;
        this.miniMessage = miniMessage;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onTag(FightTagEvent event) {
        if (!this.settings.enabled) {
            return;
        }

        Player player = this.server.getPlayer(event.getPlayer());
        if (player == null) {
            return;
        }

        if (this.bossBars.containsKey(event.getPlayer())) {
            return;
        }

        BossBar bossBar = BossBar.bossBar(
            this.miniMessage.deserialize(this.settings.title.replace("{TIME}", "0s")),
            1.0f,
            BossBar.Color.valueOf(this.settings.color.name()),
            BossBar.Overlay.valueOf(this.settings.style.name())
        );

        player.showBossBar(bossBar);
        this.bossBars.put(event.getPlayer(), bossBar);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onUntag(FightUntagEvent event) {
        if (!this.settings.enabled) {
            return;
        }

        this.removeBossBar(event.getPlayer());
    }

    public void updateBossBars() {
        if (!this.settings.enabled) {
            return;
        }

        for (Map.Entry<UUID, BossBar> entry : this.bossBars.entrySet()) {
            UUID uuid = entry.getKey();
            BossBar bossBar = entry.getValue();

            FightTag tag = this.fightManager.getTag(uuid);
            if (tag == null || tag.isExpired()) {
                Player player = this.server.getPlayer(uuid);
                if (player != null) {
                    player.hideBossBar(bossBar);
                }
                this.bossBars.remove(uuid);
                return;
            }

            String timeStr = DurationUtil.format(tag.getRemainingDuration(), true);
            float progress = Math.min(1.0f, tag.getRemainingDuration().toMillis() / (float) this.fightManager.getTag(uuid).getEndOfCombatLog().toEpochMilli());

            bossBar.name(this.miniMessage.deserialize(this.settings.title.replace("{TIME}", timeStr)));
            bossBar.progress(Math.max(0.01f, progress));
        }
    }

    public void removeAll() {
        for (Map.Entry<UUID, BossBar> entry : this.bossBars.entrySet()) {
            Player player = this.server.getPlayer(entry.getKey());
            if (player != null) {
                player.hideBossBar(entry.getValue());
            }
        }
        this.bossBars.clear();
    }

    private void removeBossBar(UUID uuid) {
        BossBar bossBar = this.bossBars.remove(uuid);
        if (bossBar == null) {
            return;
        }

        Player player = this.server.getPlayer(uuid);
        if (player != null) {
            player.hideBossBar(bossBar);
        }
    }
}
