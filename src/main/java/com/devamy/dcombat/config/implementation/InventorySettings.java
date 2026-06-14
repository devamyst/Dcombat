package com.devamy.dcombat.config.implementation;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public class InventorySettings extends OkaeriConfig {
    public InventoryAccessMode inventoryAccessMode = InventoryAccessMode.ALLOW_ALL;

    public List<InventoryType> restrictedInventoryTypes = List.of(
        InventoryType.CHEST,
        InventoryType.ENDER_CHEST,
        InventoryType.BARREL,
        InventoryType.SHULKER_BOX
    );
}
