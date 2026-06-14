package com.devamy.dcombat.config.implementation;

import com.devamy.dcombat.WhitelistBlacklistMode;
import eu.okaeri.configs.OkaeriConfig;
import java.util.List;

public class CommandSettings extends OkaeriConfig {
    public WhitelistBlacklistMode commandRestrictionMode = WhitelistBlacklistMode.BLACKLIST;

    public List<String> restrictedCommands = List.of(
        "gamemode",
        "spawn",
        "tp",
        "tpa",
        "tpaccept"
    );
}
