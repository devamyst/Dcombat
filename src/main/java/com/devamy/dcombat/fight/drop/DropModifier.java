package com.devamy.dcombat.fight.drop;

public interface DropModifier {

    DropType getDropType();

    DropResult modifyDrop(Drop drop);

}
