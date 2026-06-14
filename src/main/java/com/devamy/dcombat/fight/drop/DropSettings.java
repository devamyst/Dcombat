package com.devamy.dcombat.fight.drop;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.event.EventPriority;

public class DropSettings extends OkaeriConfig {

    public EventPriority dropEventPriority = EventPriority.NORMAL;

    public DropType dropType = DropType.UNCHANGED;

    public int dropItemPercent = 100;

    public int playersHealthPercentClamp = 20;

    public boolean affectExperience = false;
}
