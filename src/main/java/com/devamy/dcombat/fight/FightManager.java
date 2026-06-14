package com.devamy.dcombat.fight;

import com.devamy.dcombat.fight.event.CauseOfTag;
import com.devamy.dcombat.fight.event.CauseOfUnTag;
import com.devamy.dcombat.fight.event.FightTagEvent;
import com.devamy.dcombat.fight.event.FightUntagEvent;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface FightManager {

    boolean isInCombat(UUID player);

    FightTag getTag(UUID target);

    Collection<FightTag> getFights();

    @ApiStatus.Experimental
    FightTagEvent tag(UUID target, Duration delay, CauseOfTag causeOfTag, @Nullable UUID tagger);

    FightTagEvent tag(UUID target, Duration delay, CauseOfTag causeOfTag);

    FightUntagEvent untag(UUID player, CauseOfUnTag causeOfUnTag);

    void untagAll();
}
