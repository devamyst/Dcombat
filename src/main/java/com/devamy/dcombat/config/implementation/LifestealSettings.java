package com.devamy.dcombat.config.implementation;

import eu.okaeri.configs.OkaeriConfig;

public class LifestealSettings extends OkaeriConfig {

    public boolean enabled = false;

    public int heartsToSteal = 1;

    public boolean stealOnlyInCombat = true;

    public boolean stealOnKill = true;

    public boolean stealOnDeath = false;

    public boolean eliminateOnZeroHearts = true;

    public boolean notifyOnKill = true;
}
