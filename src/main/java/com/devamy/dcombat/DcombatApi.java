package com.devamy.dcombat;

import com.devamy.dcombat.border.BorderService;
import com.devamy.dcombat.fight.drop.DropKeepInventoryService;
import com.devamy.dcombat.fight.FightManager;
import com.devamy.dcombat.fight.drop.DropService;
import com.devamy.dcombat.fight.effect.FightEffectService;
import com.devamy.dcombat.fight.pearl.PearlService;
import com.devamy.dcombat.fight.stats.FightStatsService;
import com.devamy.dcombat.fight.trident.TridentService;
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

    BorderService getBorderService();

    TridentService getTridentService();

    FightStatsService getFightStatsService();

}
