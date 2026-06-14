package com.devamy.dcombat.border.animation.block;

import eu.okaeri.configs.OkaeriConfig;
import java.time.Duration;

public class BlockSettings extends OkaeriConfig {

        public boolean enabled = true;

        public BlockType type = BlockType.RAINBOW_GLASS;

        public Duration updateDelay = Duration.ofMillis(250);

        public Duration chunkCacheDelay = Duration.ofMillis(300);

}
