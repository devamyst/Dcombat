package com.devamy.dcombat.fight.drop;

import com.devamy.dcombat.event.DynamicListener;
import com.devamy.dcombat.fight.FightManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.UUID;

public class DropController implements DynamicListener<PlayerDeathEvent> {

    private final DropService dropService;
    private final DropKeepInventoryService keepInventoryManager;
    private final DropSettings dropSettings;
    private final FightManager fightManager;
    private final MiniMessage miniMessage;

    public DropController(
        DropService dropService,
        DropKeepInventoryService keepInventoryManager,
        DropSettings dropSettings,
        FightManager fightManager, MiniMessage miniMessage
    ) {
        this.dropService = dropService;
        this.keepInventoryManager = keepInventoryManager;
        this.dropSettings = dropSettings;
        this.fightManager = fightManager;
        this.miniMessage = miniMessage;
    }

    @Override
    public void onEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();
        DropType dropType = this.dropSettings.dropType;
        boolean inCombat = this.fightManager.isInCombat(uuid);

        if (dropType == DropType.UNCHANGED || !inCombat) {
            return;
        }

        List<ItemStack> drops = event.getDrops();

        Drop drop = Drop.builder()
            .player(player)
            .killer(player.getKiller())
            .droppedItems(drops)
            .droppedExp(player.getTotalExperience())
            .build();

        DropResult result = this.dropService.modify(dropType, drop);

        if (result == null) {
            return;
        }

        drops.clear();
        drops.addAll(result.droppedItems());

        this.keepInventoryManager.addItems(uuid, result.removedItems());

        if (this.dropSettings.affectExperience) {
            event.setDroppedExp(result.droppedExp());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerUniqueId = player.getUniqueId();

        if (this.keepInventoryManager.hasItems(playerUniqueId)) {
            PlayerInventory playerInventory = player.getInventory();

            ItemStack[] itemsToGive = this.keepInventoryManager.nextItems(playerUniqueId)
                .toArray(new ItemStack[0]);

            playerInventory.addItem(itemsToGive);
        }
    }
}
