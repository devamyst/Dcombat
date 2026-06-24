package com.devamy.dcombat.config.implementation;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class BossBarSettings extends OkaeriConfig {

    public boolean enabled = true;

    public BarColor color = BarColor.RED;

    public BarStyle style = BarStyle.SOLID;

    public String title = "<red>Combat ends in: {TIME}</red>";
}
