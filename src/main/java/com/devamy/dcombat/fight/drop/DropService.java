package com.devamy.dcombat.fight.drop;

public interface DropService {

    DropResult modify(DropType dropType, Drop drop);

    void registerModifier(DropModifier dropModifier);
}
