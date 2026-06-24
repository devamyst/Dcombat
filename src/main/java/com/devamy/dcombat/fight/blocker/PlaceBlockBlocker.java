package com.devamy.dcombat.fight.blocker;

import com.devamy.dcombat.config.implementation.PluginConfig;
import com.devamy.dcombat.config.implementation.BlockPlacementSettings;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.notification.NoticeService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;
import java.util.UUID;

public class PlaceBlockBlocker implements Listener {

    private final FightManager fightManager;
    private final NoticeService noticeService;
    private final PluginConfig config;

    public PlaceBlockBlocker(FightManager fightManager, NoticeService noticeService, PluginConfig config) {
        this.fightManager = fightManager;
        this.noticeService = noticeService;
        this.config = config;
    }

    @EventHandler
    void onPlace(BlockPlaceEvent event) {
        if (!this.config.blockPlacement.disableBlockPlacing) {
            return;
        }

        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if (!this.fightManager.isInCombat(uniqueId)) {
            return;
        }

        Block block = event.getBlock();
        int level = block.getY();

        List<Material> restrictedBlocks = this.config.blockPlacement.restrictedBlockTypes;

        if (restrictedBlocks.contains(block.getType())) {
            event.setCancelled(true);
            this.noticeService.create()
                .player(uniqueId)
                .notice(this.config.messagesSettings.blockPlacingBlockedDuringCombat)
                .placeholder("{Y}", String.valueOf(this.config.blockPlacement.blockPlacementYCoordinate))
                .placeholder("{MODE}", this.config.blockPlacement.blockPlacementModeDisplayName)
                .send();
            return;
        }

        if (this.isPlacementBlocked(level)) {
            event.setCancelled(true);
            this.noticeService.create()
                .player(uniqueId)
                .notice(this.config.messagesSettings.blockPlacingBlockedDuringCombat)
                .placeholder("{Y}", String.valueOf(this.config.blockPlacement.blockPlacementYCoordinate))
                .placeholder("{MODE}", this.config.blockPlacement.blockPlacementModeDisplayName)
                .send();
        }
    }

    private boolean isPlacementBlocked(int level) {
        return this.config.blockPlacement.blockPlacementMode == BlockPlacementSettings.BlockPlacingMode.ABOVE
            ? level > this.config.blockPlacement.blockPlacementYCoordinate
            : level < this.config.blockPlacement.blockPlacementYCoordinate;
    }

}
