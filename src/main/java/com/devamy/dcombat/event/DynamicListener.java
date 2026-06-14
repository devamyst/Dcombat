package com.devamy.dcombat.event;

import org.bukkit.event.Listener;

public interface DynamicListener<E> extends Listener {

    void onEvent(E event);

}
