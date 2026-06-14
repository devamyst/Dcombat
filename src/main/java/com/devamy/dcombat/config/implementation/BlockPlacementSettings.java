package com.devamy.dcombat.config.implementation;

import eu.okaeri.configs.OkaeriConfig;
import java.util.List;
import org.bukkit.Material;

public class BlockPlacementSettings extends OkaeriConfig {
    public boolean disableBlockPlacing = true;

    public BlockPlacingMode blockPlacementMode = BlockPlacingMode.ABOVE;

    public String blockPlacementModeDisplayName = "above";

    public int blockPlacementYCoordinate = 40;
    public List<Material> restrictedBlockTypes = List.of();

    public enum BlockPlacingMode {
        ABOVE,
        BELOW
    }
}
