package com.devamy.dcombat.config.implementation;

import eu.okaeri.configs.OkaeriConfig;
import java.util.Collections;
import java.util.List;

public class RegionSettings extends OkaeriConfig {
    public List<String> blockedRegions = Collections.singletonList("your_region");

    public boolean preventPvpInRegions = true;

    public int restrictedRegionRadius = 10;
}
