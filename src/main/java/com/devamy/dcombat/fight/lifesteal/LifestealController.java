package com.devamy.dcombat.fight.lifesteal;

import com.devamy.dcombat.config.implementation.LifestealSettings;
import com.devamy.dcombat.config.implementation.MessagesSettings;
import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.notification.NoticeService;
import com.eternalcode.multification.notice.Notice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.lang.reflect.Method;

public class LifestealController implements Listener {

    private final PluginConfig pluginConfig;
    private final FightManager fightManager;
    private final NoticeService noticeService;
    private Object lifestealApi;
    private Method getPlayerHeartsMethod;
    private Method removePlayerHeartsMethod;
    private Method addPlayerHeartsMethod;
    private Method setPlayerHeartsMethod;
    private Method setPlayerEliminatedMethod;

    public LifestealController(PluginConfig pluginConfig, FightManager fightManager, NoticeService noticeService) {
        this.pluginConfig = pluginConfig;
        this.fightManager = fightManager;
        this.noticeService = noticeService;
        tryResolveApi();
    }

    private void tryResolveApi() {
        try {
            Object lifestealCore = Bukkit.getPluginManager().getPlugin("LifestealCore");
            if (lifestealCore == null) {
                return;
            }
            Method getAPIMethod = lifestealCore.getClass().getMethod("getAPI");
            this.lifestealApi = getAPIMethod.invoke(lifestealCore);

            Class<?> apiClass = this.lifestealApi.getClass();
            this.getPlayerHeartsMethod = apiClass.getMethod("getPlayerHearts", java.util.UUID.class);
            this.removePlayerHeartsMethod = apiClass.getMethod("removePlayerHearts", java.util.UUID.class, int.class, boolean.class);
            this.addPlayerHeartsMethod = apiClass.getMethod("addPlayerHearts", java.util.UUID.class, int.class);
            this.setPlayerHeartsMethod = apiClass.getMethod("setPlayerHearts", java.util.UUID.class, int.class);
            this.setPlayerEliminatedMethod = apiClass.getMethod("setPlayerEliminated", java.util.UUID.class, boolean.class, boolean.class);
        } catch (Exception ignored) {
            this.lifestealApi = null;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        LifestealSettings settings = this.pluginConfig.lifesteal;
        if (!settings.enabled) {
            return;
        }

        if (this.lifestealApi == null) {
            return;
        }

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) {
            return;
        }

        boolean victimInCombat = this.fightManager.isInCombat(victim.getUniqueId());
        boolean killerInCombat = this.fightManager.isInCombat(killer.getUniqueId());

        boolean steal = settings.stealOnKill
            && (!settings.stealOnlyInCombat || killerInCombat);

        if (!steal) {
            steal = settings.stealOnDeath
                && (!settings.stealOnlyInCombat || victimInCombat);
        }

        if (!steal) {
            return;
        }

        try {
            int heartsToSteal = settings.heartsToSteal;
            int victimHearts = (int) this.getPlayerHeartsMethod.invoke(this.lifestealApi, victim.getUniqueId());

            if (victimHearts - heartsToSteal <= 0) {
                if (settings.eliminateOnZeroHearts) {
                    this.setPlayerEliminatedMethod.invoke(this.lifestealApi, victim.getUniqueId(), true, false);
                }
                this.setPlayerHeartsMethod.invoke(this.lifestealApi, victim.getUniqueId(), 0);
            } else {
                this.removePlayerHeartsMethod.invoke(this.lifestealApi, victim.getUniqueId(), heartsToSteal, false);
                this.addPlayerHeartsMethod.invoke(this.lifestealApi, killer.getUniqueId(), heartsToSteal);
            }

            if (settings.notifyOnKill) {
                MessagesSettings messages = this.pluginConfig.messagesSettings;
                String msg = messages.lifestealHeartsStolen
                    .replace("{PLAYER}", victim.getName())
                    .replace("{KILLER}", killer.getName())
                    .replace("{HEARTS}", String.valueOf(heartsToSteal));

                this.noticeService.create()
                    .viewer(killer)
                    .notice(Notice.chat(msg))
                    .send();
            }
        } catch (Exception ignored) {
        }
    }
}
