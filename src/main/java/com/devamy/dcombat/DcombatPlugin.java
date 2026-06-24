package com.devamy.dcombat;

import com.devamy.dcombat.border.BorderService;
import com.devamy.dcombat.border.BorderServiceImpl;
import com.devamy.dcombat.border.BorderTriggerController;
import com.devamy.dcombat.border.animation.block.BorderBlockController;
import com.devamy.dcombat.border.animation.particle.ParticleController;
import com.devamy.dcombat.bridge.BridgeService;
import com.devamy.dcombat.config.ConfigService;
import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.config.migration.ConfigMigrator;
import com.devamy.dcombat.crystalpvp.EndCrystalListener;
import com.devamy.dcombat.crystalpvp.RespawnAnchorListener;
import com.devamy.dcombat.event.EventManager;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.fight.FightManagerImpl;
import com.devamy.dcombat.fight.FightTagCommand;
import com.devamy.dcombat.fight.FightTask;
import com.devamy.dcombat.fight.blocker.CommandsBlocker;
import com.devamy.dcombat.fight.blocker.ElytraBlocker;
import com.devamy.dcombat.fight.blocker.ElytraEquipBlocker;
import com.devamy.dcombat.fight.blocker.FlyingBlocker;
import com.devamy.dcombat.fight.bossbar.BossBarController;
import com.devamy.dcombat.fight.controller.FightBypassAdminController;
import com.devamy.dcombat.fight.controller.FightBypassCreativeController;
import com.devamy.dcombat.fight.controller.FightBypassPermissionController;
import com.devamy.dcombat.fight.blocker.InventoryContainersBlocker;
import com.devamy.dcombat.fight.controller.FightMessageController;
import com.devamy.dcombat.fight.controller.FightTagController;
import com.devamy.dcombat.fight.controller.FightUnTagController;
import com.devamy.dcombat.fight.death.DeathFlareController;
import com.devamy.dcombat.fight.death.DeathLightningController;
import com.devamy.dcombat.fight.drop.DropController;
import com.devamy.dcombat.fight.drop.DropKeepInventoryService;
import com.devamy.dcombat.fight.drop.DropKeepInventoryServiceImpl;
import com.devamy.dcombat.fight.drop.DropService;
import com.devamy.dcombat.fight.drop.DropServiceImpl;
import com.devamy.dcombat.fight.drop.impl.PercentDropModifier;
import com.devamy.dcombat.fight.drop.impl.PlayersHealthDropModifier;
import com.devamy.dcombat.fight.blocker.PlaceBlockBlocker;
import com.devamy.dcombat.fight.effect.FightEffectController;
import com.devamy.dcombat.fight.effect.FightEffectService;
import com.devamy.dcombat.fight.effect.FightEffectServiceImpl;
import com.devamy.dcombat.fight.firework.FireworkController;
import com.devamy.dcombat.fight.knockback.KnockbackRegionController;
import com.devamy.dcombat.fight.knockback.KnockbackService;
import com.devamy.dcombat.fight.logout.LogoutAlertController;
import com.devamy.dcombat.fight.logout.LogoutController;
import com.devamy.dcombat.fight.logout.LogoutService;
import com.devamy.dcombat.fight.pearl.PearlController;
import com.devamy.dcombat.fight.pearl.PearlService;
import com.devamy.dcombat.fight.pearl.PearlServiceImpl;
import com.devamy.dcombat.fight.stats.FightStatsService;
import com.devamy.dcombat.fight.tagout.FightTagOutCommand;
import com.devamy.dcombat.fight.tagout.FightTagOutController;
import com.devamy.dcombat.fight.tagout.FightTagOutService;
import com.devamy.dcombat.fight.tagout.FightTagOutServiceImpl;
import com.devamy.dcombat.fight.trident.TridentController;
import com.devamy.dcombat.fight.trident.TridentService;
import com.devamy.dcombat.fight.trident.TridentServiceImpl;
import com.devamy.dcombat.handler.InvalidUsageHandlerImpl;
import com.devamy.dcombat.handler.MissingPermissionHandlerImpl;
import com.devamy.dcombat.fight.lifesteal.LifestealController;
import com.devamy.dcombat.notification.NoticeService;
import com.devamy.dcombat.region.RegionProvider;
import com.devamy.dcombat.scheduler.Scheduler;
import com.devamy.dcombat.updater.UpdaterNotificationController;
import com.devamy.dcombat.updater.UpdaterService;
import com.devamy.dcombat.WikiGenerator;
import com.eternalcode.multification.notice.Notice;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.folia.FoliaExtension;
import java.time.Duration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class DcombatPlugin extends JavaPlugin implements DcombatApi {

    private static final String FALLBACK_PREFIX = "dcombat";
    private static final int BSTATS_METRICS_ID = 17803;

    private FightManager fightManager;
    private PearlService pearlService;
    private TridentService tridentService;
    private FightTagOutService fightTagOutService;
    private FightEffectService fightEffectService;

    private DropService dropService;
    private DropKeepInventoryService dropKeepInventoryService;

    private FightStatsService fightStatsService;

    private BorderService borderService;

    private RegionProvider regionProvider;

    private LiteCommands<CommandSender> liteCommands;
    private boolean apiInitialized;


    @Override
    public void onEnable() {
        Stopwatch started = Stopwatch.createStarted();
        Server server = this.getServer();

        File dataFolder = this.getDataFolder();

        ConfigService configService = new ConfigService();

        EventManager eventManager = new EventManager(this);
        PluginConfig pluginConfig = configService.create(PluginConfig.class, new File(dataFolder, "config.yml"));
        WikiGenerator.writeWiki(dataFolder);

        Scheduler scheduler = CombatSchedulerAdapter.getAdaptiveScheduler(this);

        this.fightManager = new FightManagerImpl(eventManager);
        this.pearlService = new PearlServiceImpl(this.fightManager, pluginConfig, scheduler);
        this.tridentService = new TridentServiceImpl(pluginConfig);
        this.fightTagOutService = new FightTagOutServiceImpl();
        this.fightEffectService = new FightEffectServiceImpl();
        this.fightStatsService = new FightStatsService();

        LogoutService logoutService = new LogoutService();

        this.dropService = new DropServiceImpl();
        this.dropKeepInventoryService = new DropKeepInventoryServiceImpl();

        UpdaterService updaterService = new UpdaterService(this.getDescription());

        MiniMessage miniMessage = MiniMessage.miniMessage();

        NoticeService noticeService = new NoticeService(pluginConfig, miniMessage);

        BridgeService bridgeService = new BridgeService(
            pluginConfig,
            server.getPluginManager(),
            this.getLogger(),
            this,
            this.fightManager
        );
        bridgeService.init(server);

        this.regionProvider = bridgeService.getRegionProvider();
        this.borderService = new BorderServiceImpl(scheduler, server, regionProvider, eventManager, () -> pluginConfig.border);
        KnockbackService knockbackService = new KnockbackService(pluginConfig, scheduler, regionProvider);

        BossBarController bossBarController = new BossBarController(this.fightManager, pluginConfig.bossBar, server, miniMessage);

        ConfigMigrator configMigrator = new ConfigMigrator(this.getLogger());
        configMigrator.migrate(pluginConfig);

        this.liteCommands = LiteBukkitFactory.builder(FALLBACK_PREFIX, this, server)
            .message(LiteBukkitMessages.PLAYER_NOT_FOUND, pluginConfig.messagesSettings.playerNotFound)
            .message(LiteBukkitMessages.PLAYER_ONLY, pluginConfig.messagesSettings.admin.onlyForPlayers)

            .invalidUsage(new InvalidUsageHandlerImpl(pluginConfig, noticeService))
            .missingPermission(new MissingPermissionHandlerImpl(pluginConfig, noticeService))

            .commands(
                new FightTagCommand(this.fightManager, noticeService, pluginConfig, this.fightStatsService),
                new FightTagOutCommand(this.fightTagOutService, noticeService, pluginConfig),
                new DcombatReloadCommand(configService, noticeService),
                new com.devamy.dcombat.fight.stats.CombatStatsCommand(this.fightStatsService, noticeService)
            )

            .extension(new FoliaExtension(this))

            .result(Notice.class, (invocation, result, chain) -> noticeService.create()
                .viewer(invocation.sender())
                .notice(result)
                .send())

            .build();

        FightTask fightTask = new FightTask(server, pluginConfig, this.fightManager, noticeService, this.fightStatsService, bossBarController);
        scheduler.timer(fightTask, Duration.ofSeconds(1), Duration.ofSeconds(1));

        new Metrics(this, BSTATS_METRICS_ID);

        Stream.of(
            new PercentDropModifier(pluginConfig.drop),
            new PlayersHealthDropModifier(pluginConfig.drop, logoutService)
        ).forEach(this.dropService::registerModifier);

        eventManager.subscribe(
            new FightTagController(this.fightManager, pluginConfig, this.fightStatsService),
            new FightUnTagController(this.fightManager, pluginConfig, logoutService, this.fightStatsService),
            new FightBypassAdminController(server, pluginConfig),
            new FightBypassPermissionController(server, pluginConfig),
            new FightBypassCreativeController(server, pluginConfig),
            new PlaceBlockBlocker(this.fightManager, noticeService, pluginConfig),
            new PearlController(pluginConfig, this.pearlService, noticeService, fightManager),
            new TridentController(pluginConfig, noticeService, this.fightManager, this.tridentService, server),
            new DeathFlareController(pluginConfig, server, this),
            new DeathLightningController(pluginConfig, server),
            new UpdaterNotificationController(updaterService, pluginConfig, miniMessage),
            new KnockbackRegionController(noticeService, this.regionProvider, this.fightManager, knockbackService, server),
            new FightEffectController(pluginConfig.effect, this.fightEffectService, this.fightManager, server),
            new FightTagOutController(this.fightTagOutService),
            new FightMessageController(this.fightManager, noticeService, pluginConfig, server),
            new BorderTriggerController(this.borderService, () -> pluginConfig.border, fightManager, server, scheduler),
            new ParticleController(this.borderService, () -> pluginConfig.border.particle, scheduler, server),
            new BorderBlockController(this.borderService, () -> pluginConfig.border.block, scheduler, server),
            new EndCrystalListener(this, this.fightManager, pluginConfig),
            new RespawnAnchorListener(this, this.fightManager, pluginConfig),
            new FireworkController(this.fightManager, pluginConfig, noticeService),
            new InventoryContainersBlocker(this.fightManager, pluginConfig, noticeService),
            new CommandsBlocker(this.fightManager, noticeService, pluginConfig),
            new ElytraBlocker(this.fightManager, pluginConfig),
            new ElytraEquipBlocker(this.fightManager, noticeService, pluginConfig, server),
            new FlyingBlocker(this.fightManager, pluginConfig, server),
            new LifestealController(pluginConfig, this.fightManager, noticeService),
            bossBarController,
            new LogoutAlertController(this.fightManager, server, noticeService, pluginConfig)
        );

        eventManager.subscribe(
            PlayerDeathEvent.class,
            pluginConfig.drop.dropEventPriority,
            new DropController(dropService, dropKeepInventoryService, pluginConfig.drop, fightManager, miniMessage)
        );

        eventManager.subscribe(
            PlayerQuitEvent.class,
            pluginConfig.combat.quitPunishmentEventPriority,
            new LogoutController(this.fightManager, logoutService, noticeService, pluginConfig)
        );

        DcombatProvider.initialize(this);
        this.apiInitialized = true;

        long millis = started.elapsed(TimeUnit.MILLISECONDS);
        this.getLogger().info("Successfully loaded Dcombat in " + millis + "ms");
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        if (this.fightManager != null) {
            this.fightManager.untagAll();
        }

        if (this.apiInitialized) {
            DcombatProvider.deinitialize();
            this.apiInitialized = false;
        }
    }

    @Override
    public FightManager getFightManager() {
        return this.fightManager;
    }

    @Override
    public RegionProvider getRegionProvider() {
        return this.regionProvider;
    }

    @Override
    public PearlService getFightPearlService() {
        return this.pearlService;
    }

    @Override
    public FightTagOutService getFightTagOutService() {
        return this.fightTagOutService;
    }

    @Override
    public FightEffectService getFightEffectService() {
        return this.fightEffectService;
    }

    @Override
    public DropService getDropService() {
        return this.dropService;
    }

    @Override
    public DropKeepInventoryService getDropKeepInventoryService() {
        return this.dropKeepInventoryService;
    }

    @Override
    public BorderService getBorderService() {
        return this.borderService;
    }

    @Override
    public TridentService getTridentService() {
        return this.tridentService;
    }

    @Override
    public FightStatsService getFightStatsService() {
        return this.fightStatsService;
    }
}
