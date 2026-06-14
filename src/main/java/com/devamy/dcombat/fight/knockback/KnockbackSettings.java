package com.devamy.dcombat.fight.knockback;

import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import java.time.Duration;
import java.util.EnumSet;
import java.util.Set;

public class KnockbackSettings extends OkaeriConfig {

        public double multiplier = 1.0;

        public double vertical = 0.2;

        public ForceTeleport forceTeleport = new ForceTeleport();

    public static class ForceTeleport extends OkaeriConfig {

                public Duration delay = Duration.ofSeconds(1);

                public Set<XMaterial> unsafeGroundBlocks = EnumSet.of(
            XMaterial.BARRIER,
            XMaterial.LAVA,
            XMaterial.WATER,
            XMaterial.CACTUS,
            XMaterial.MAGMA_BLOCK,
            XMaterial.FIRE,
            XMaterial.SOUL_FIRE,
            XMaterial.COBWEB,
            XMaterial.SWEET_BERRY_BUSH,
            XMaterial.BEDROCK,
            XMaterial.TNT,
            XMaterial.SEAGRASS,
            XMaterial.TALL_SEAGRASS,
            XMaterial.BUBBLE_COLUMN,
            XMaterial.POWDER_SNOW,
            XMaterial.WITHER_ROSE
        );

                public Set<XMaterial> airBlocks = EnumSet.of(
            XMaterial.AIR,
            XMaterial.CAVE_AIR,
            XMaterial.VOID_AIR,
            XMaterial.TALL_SEAGRASS,
            XMaterial.SEAGRASS,
            XMaterial.SHORT_GRASS,
            XMaterial.TALL_GRASS,
            XMaterial.VINE,
            XMaterial.STRUCTURE_VOID,
            XMaterial.DEAD_BUSH,
            XMaterial.DANDELION,
            XMaterial.POPPY,
            XMaterial.BLUE_ORCHID,
            XMaterial.ALLIUM,
            XMaterial.AZURE_BLUET,
            XMaterial.RED_TULIP,
            XMaterial.ORANGE_TULIP,
            XMaterial.WHITE_TULIP,
            XMaterial.PINK_TULIP,
            XMaterial.OXEYE_DAISY,
            XMaterial.CORNFLOWER,
            XMaterial.LILY_OF_THE_VALLEY,
            XMaterial.SUNFLOWER,
            XMaterial.LILAC,
            XMaterial.ROSE_BUSH,
            XMaterial.PEONY,
            XMaterial.WITHER_ROSE,
            XMaterial.LARGE_FERN,
            XMaterial.RAIL,
            XMaterial.POWERED_RAIL,
            XMaterial.DETECTOR_RAIL,
            XMaterial.ACTIVATOR_RAIL,
            XMaterial.REDSTONE_WIRE,
            XMaterial.COMPARATOR,
            XMaterial.REPEATER,
            XMaterial.LEVER,
            XMaterial.STRING,
            XMaterial.SNOW,
            XMaterial.CRIMSON_ROOTS,
            XMaterial.WARPED_ROOTS,
            XMaterial.CRIMSON_FUNGUS,
            XMaterial.WARPED_FUNGUS,
            XMaterial.TORCH,
            XMaterial.WALL_TORCH,
            XMaterial.REDSTONE_TORCH,
            XMaterial.REDSTONE_WALL_TORCH,
            XMaterial.SOUL_TORCH,
            XMaterial.SOUL_WALL_TORCH
        );

    }


}
