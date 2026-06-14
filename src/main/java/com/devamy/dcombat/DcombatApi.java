package com.devamy.dcombat;

import com.devamy.dcombat.fight.drop.DropKeepInventoryService;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.fight.drop.DropService;
import com.devamy.dcombat.fight.effect.FightEffectService;
import com.devamy.dcombat.fight.pearl.PearlService;
import com.devamy.dcombat.region.RegionProvider;
import com.devamy.dcombat.fight.tagout.FightTagOutService;

public interface DcombatApi {

    FightManager getFightManager();

    RegionProvider getRegionProvider();

    PearlService getFightPearlService();

    FightTagOutService getFightTagOutService();

    FightEffectService getFightEffectService();

    DropService getDropService();

    DropKeepInventoryService getDropKeepInventoryService();

}
