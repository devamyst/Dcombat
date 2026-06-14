package com.devamy.dcombat.border.event;

import com.devamy.dcombat.border.BorderPoint;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BorderShowAsyncEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private Set<BorderPoint> points;

    public BorderShowAsyncEvent(Player player, Set<BorderPoint> points) {
        super(true);
        this.player = player;
        this.points = points;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<BorderPoint> getPoints() {
        return points;
    }

    public void setPoints(Set<BorderPoint> points) {
        this.points = points;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
