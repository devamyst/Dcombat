package com.devamy.dcombat.fight.trident;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import java.time.Duration;

public class TridentSettings extends OkaeriConfig {

    public boolean tridentRiptideDisabledDuringCombat = false;

    public boolean tridentRiptideExtendsCombatTag = false;

    public Duration tridentRiptideDelay = Duration.ofSeconds(10);

    public Notice tridentRiptideBlocked = Notice.builder()
        .chat("<red>Using riptide is prohibited during combat!")
        .build();

    public Notice tridentRiptideOnCooldown = Notice.builder()
        .chat("<red>You must wait {TIME} before next riptide!")
        .build();
}
