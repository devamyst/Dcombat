package com.devamy.dcombat.fight.drop.impl;

import com.devamy.dcombat.fight.drop.Drop;
import com.devamy.dcombat.fight.drop.DropModifier;
import com.devamy.dcombat.fight.drop.DropResult;
import com.devamy.dcombat.fight.drop.DropSettings;
import com.devamy.dcombat.fight.drop.DropType;
import com.devamy.dcombat.util.InventoryUtil;
import com.devamy.dcombat.util.MathUtil;
import com.devamy.dcombat.util.RemoveItemResult;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PercentDropModifier implements DropModifier {

    private final DropSettings settings;

    public PercentDropModifier(DropSettings settings) {
        this.settings = settings;
    }

    @Override
    public DropType getDropType() {
        return DropType.PERCENT;
    }

    @Override
    public DropResult modifyDrop(Drop drop) {
        int dropItemPercent = 100 - MathUtil.clamp(this.settings.dropItemPercent, 0, 100);
        List<ItemStack> droppedItems = drop.getDroppedItems();

        int itemsToDelete = InventoryUtil.calculateItemsToDelete(dropItemPercent, droppedItems, ItemStack::getAmount);
        int droppedExp = MathUtil.getRoundedCountFromPercentage(dropItemPercent, drop.getDroppedExp());

        RemoveItemResult result = InventoryUtil.removeRandomItems(droppedItems, itemsToDelete);

        return new DropResult(result.restItems(), result.removedItems(), droppedExp);
    }
}
