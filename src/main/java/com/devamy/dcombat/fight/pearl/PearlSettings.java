package com.devamy.dcombat.fight.pearl;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import java.time.Duration;

public class PearlSettings extends OkaeriConfig {

    public boolean pearlThrowDamageEnabled = true;

    public boolean pearlThrowDisabledDuringCombat = true;

    public boolean pearlCooldownEnabled = false;

    public boolean pearlExtendsCombatTag = false;

    public Duration pearlThrowDelay = Duration.ofSeconds(3);

    public Notice pearlThrowBlockedDuringCombat = Notice.builder()
        .chat("<red>Throwing ender pearls is prohibited during combat!")
        .build();

    public Notice pearlThrowBlockedDelayDuringCombat = Notice.builder()
        .chat("<red>You must wait {TIME} before next throw!")
        .build();
}
