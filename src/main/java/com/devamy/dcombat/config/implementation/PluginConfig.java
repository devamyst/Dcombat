package com.devamy.dcombat.config.implementation;

import com.devamy.dcombat.border.BorderSettings;
import com.devamy.dcombat.fight.death.DeathSettings;
import com.devamy.dcombat.fight.drop.DropSettings;
import com.devamy.dcombat.fight.effect.FightEffectSettings;
import com.devamy.dcombat.fight.knockback.KnockbackSettings;
import com.devamy.dcombat.fight.pearl.PearlSettings;
import com.devamy.dcombat.fight.trident.TridentSettings;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import java.time.Duration;
import java.util.List;

public class PluginConfig extends OkaeriConfig {

    @Exclude
    public int configVersion = 1;

    public Settings settings = new Settings();

    public PearlSettings pearl = new PearlSettings();

    public TridentSettings trident = new TridentSettings();

    public FightEffectSettings effect = new FightEffectSettings();

    public DeathSettings death = new DeathSettings();

    public DropSettings drop = new DropSettings();

    public KnockbackSettings knockback = new KnockbackSettings();

    public BorderSettings border = new BorderSettings();

    public BlockPlacementSettings blockPlacement = new BlockPlacementSettings();

    public CrystalPvpSettings crystalPvp = new CrystalPvpSettings();

    public CommandSettings commands = new CommandSettings();

    public AdminSettings admin = new AdminSettings();

    public RegionSettings regions = new RegionSettings();

    public CombatSettings combat = new CombatSettings();

    public InventorySettings inventory = new InventorySettings();

    public PlaceholderSettings placeholders = new PlaceholderSettings();

    public MessagesSettings messagesSettings = new MessagesSettings();

    public LifestealSettings lifesteal = new LifestealSettings();

    public BossBarSettings bossBar = new BossBarSettings();

    public CombatStatsSettings combatStats = new CombatStatsSettings();

    public static class Settings extends OkaeriConfig {
        public Duration combatTimerDuration = Duration.ofSeconds(20);

        public List<String> ignoredWorlds = List.of(
            "your_world"
        );
    }
}
