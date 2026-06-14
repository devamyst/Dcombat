package com.devamy.dcombat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class WikiGenerator {

    private static final String FILE_NAME = "wiki.txt";

    private static final String CONTENT = """
Dcombat - Complete Configuration Guide
======================================

This file documents every configuration section in config.yml.
Place this file alongside config.yml in your plugin data folder.

----------------------------------------------------------------
1. SETTINGS
----------------------------------------------------------------
notifyAboutUpdates  (boolean, default: true)
  Show a notification to op players when an update is available.

combatTimerDuration  (Duration, default: 20s)
  How long a player stays in combat tag after their last PvP interaction.
  Format: number + s/m/h (e.g. 15s, 1m, 30m).

ignoredWorlds  (list of strings, default: ["your_world"])
  Worlds where combat tagging is completely disabled.
  All PvP in these worlds will not trigger combat tags.

----------------------------------------------------------------
2. PEARL SETTINGS
----------------------------------------------------------------
pearlThrowDamageEnabled  (boolean, default: true)
  Whether throwing an ender pearl causes damage when it lands.

pearlThrowDisabledDuringCombat  (boolean, default: true)
  Prevent players from throwing ender pearls while in combat.

pearlCooldownEnabled  (boolean, default: false)
  Enable a cooldown between ender pearl throws.

pearlExtendsCombatTag  (boolean, default: false)
  Whether throwing a pearl while in combat refreshes the tag duration.

pearlThrowDelay  (Duration, default: 3s)
  Cooldown duration between pearl throws if cooldown is enabled.

pearlThrowBlockedDuringCombat  (Notice)
  Message shown when a player tries to throw a pearl during combat.

pearlThrowBlockedDelayDuringCombat  (Notice)
  Message shown when a player's pearl is on cooldown.

----------------------------------------------------------------
3. TRIDENT SETTINGS
----------------------------------------------------------------
tridentRiptideDisabledDuringCombat  (boolean, default: false)
  Prevent riptide use while in combat.

tridentRiptideExtendsCombatTag  (boolean, default: false)
  Whether using riptide during combat refreshes the tag duration.

tridentRiptideDelay  (Duration, default: 10s)
  Cooldown between riptide uses.

tridentRiptideBlocked  (Notice)
  Message when riptide is blocked during combat.

tridentRiptideOnCooldown  (Notice)
  Message shown when riptide is on cooldown.

----------------------------------------------------------------
4. EFFECT SETTINGS
----------------------------------------------------------------
customEffectsEnabled  (boolean, default: false)
  Apply potion effects to players while they are in combat.

customEffects  (map of potion effect -> amplifier, default: SPEED:1, RESISTANCE:0)
  The potion effects applied during combat.
  Key must be a valid PotionEffectType name.
  Value is the amplifier level (0 = level I).

----------------------------------------------------------------
5. DEATH SETTINGS
----------------------------------------------------------------
lightning:
  afterEveryDeath  (boolean, default: false)
    Strike lightning on every player death regardless of combat state.
  inCombat  (boolean, default: true)
    Strike lightning only on deaths that occur during combat.

firework:
  afterEveryDeath  (boolean, default: false)
    Spawn a firework rocket on every player death.
  inCombat  (boolean, default: false)
    Spawn firework only on combat deaths.
  power  (int, default: 2)
    Firework flight power / duration.
  fireworkType  (FireworkEffect.Type, default: BALL)
    Shape of the firework explosion (BALL, BALL_LARGE, STAR, CREEPER, BURST).
  primaryColor  (hex color, default: "#a80022")
    Primary firework color.
  fadeColor  (hex color, default: "#0a0a0a")
    Firework fade-out color.
  particlesEnabled  (boolean, default: true)
    Spawn ambient particles along with the firework.
  mainParticle  (XParticle, default: CAMPFIRE_COSY_SMOKE)
    Primary particle effect.
  mainParticleCount  (int, default: 3)
    Number of main particles per tick.
  secondaryParticle  (XParticle, default: SMALL_FLAME)
    Secondary particle effect.
  secondaryParticleCount  (int, default: 3)
    Number of secondary particles per tick.

----------------------------------------------------------------
6. DROP SETTINGS
----------------------------------------------------------------
dropEventPriority  (EventPriority, default: NORMAL)
    Bukkit event priority for the drop handler.

dropType  (enum: UNCHANGED | PERCENT | PLAYERS_HEALTH, default: UNCHANGED)
    How inventory drops are modified on combat death.
    UNCHANGED      - vanilla behavior, no modification.
    PERCENT        - drop a fixed percentage of items.
    PLAYERS_HEALTH - drop percentage depends on victim's remaining health.

dropItemPercent  (int, default: 100)
    Only used when dropType = PERCENT.
    Percentage of items to KEEP. Lower = more drops lost.
    E.g. 50 means the victim keeps 50% of items.

playersHealthPercentClamp  (int, default: 20)
    Minimum percentage of items the victim will always keep when
    dropType = PLAYERS_HEALTH. Prevents full inventory loss at 1 HP.

affectExperience  (boolean, default: false)
    Whether the drop modifier also affects dropped experience orbs.

----------------------------------------------------------------
7. KNOCKBACK SETTINGS
----------------------------------------------------------------
multiplier  (double, default: 1.0)
    Global knockback multiplier. Higher = more knockback.

vertical  (double, default: 0.2)
    Vertical knockback component.

forceTeleport:
    delay  (Duration, default: 1s)
        Delay before safe-teleport kicks in after knockback.
    unsafeGroundBlocks  (set of XMaterial)
        Block types considered unsafe to land on.
    airBlocks  (set of XMaterial)
        Block types considered air (triggers safe-teleport).

----------------------------------------------------------------
8. BORDER SETTINGS
----------------------------------------------------------------
distance  (double, default: 6.5)
    Distance from the player at which the combat border is rendered.

block:
    (Settings for block-based border animation)
    See the border/animation/block package.

particle:
    (Settings for particle-based border animation)
    See the border/animation/particle package.

----------------------------------------------------------------
9. BLOCK PLACEMENT SETTINGS
----------------------------------------------------------------
disableBlockPlacing  (boolean, default: true)
    Restrict block placement while in combat.

blockPlacementMode  (enum: ABOVE | BELOW, default: ABOVE)
    Direction restriction for block placement.
    ABOVE = cannot place blocks above the Y coordinate.
    BELOW = cannot place blocks below the Y coordinate.

blockPlacementModeDisplayName  (string, default: "above")
    Display name used in messages for the placement mode.

blockPlacementYCoordinate  (int, default: 40)
    The Y-level used as the boundary for placement restriction.

restrictedBlockTypes  (list of Material, default: [])
    Specific block materials that cannot be placed during combat.

----------------------------------------------------------------
10. CRYSTAL PVP SETTINGS
----------------------------------------------------------------
tagFromCrystals  (boolean, default: true)
    Whether end crystal explosions tag the attacker.

tagFromRespawnAnchor  (boolean, default: true)
    Whether respawn anchor explosions tag the attacker.

----------------------------------------------------------------
11. COMMAND SETTINGS
----------------------------------------------------------------
commandRestrictionMode  (enum: BLACKLIST | WHITELIST, default: BLACKLIST)
    BLACKLIST - all commands allowed except those in restrictedCommands.
    WHITELIST - only commands in restrictedCommands are allowed.

restrictedCommands  (list of strings)
    Commands affected by the restriction mode.
    Do NOT include the leading slash.

----------------------------------------------------------------
12. ADMIN SETTINGS
----------------------------------------------------------------
excludeAdminsFromCombat  (boolean, default: false)
    Players with the dcombat.bypass.admin permission are never tagged.

excludeCreativePlayersFromCombat  (boolean, default: false)
    Players in creative mode are never tagged.

----------------------------------------------------------------
13. REGION SETTINGS
----------------------------------------------------------------
blockedRegions  (list of strings, default: ["your_region"])
    Region names (WorldGuard) where combat is blocked.

preventPvpInRegions  (boolean, default: true)
    Whether to prevent PvP from starting in blocked regions.

restrictedRegionRadius  (int, default: 10)
    Radius around blocked regions where combat is also prevented.

----------------------------------------------------------------
14. COMBAT SETTINGS
----------------------------------------------------------------
releaseAttackerOnVictimDeath  (boolean, default: true)
    When the victim dies, the attacker is also untagged.

disableElytraUsage  (boolean, default: true)
    Prevent elytra usage entirely while in combat.

disableElytraOnDamage  (boolean, default: true)
    Disable elytra gliding when the player takes damage.

disableFlying  (boolean, default: true)
    Disable /fly command effect during combat.

unequipElytraOnCombat  (boolean, default: true)
    Force-unequip the chestplate slot when entering combat.

disableFireworks  (boolean, default: true)
    Prevent firework rocket usage during combat.

enableDamageCauseLogging  (boolean, default: false)
    Log damage causes to console for debugging.

damageCauseRestrictionMode  (enum: WHITELIST | BLACKLIST, default: WHITELIST)
    WHITELIST - only the listed damage causes are tracked for combat.
    BLACKLIST - all damage causes except those listed are tracked.

loggedDamageCauses  (list of DamageCause)
    Damage causes that trigger combat tagging.

ignoredProjectileTypes  (list of EntityType, default: ENDER_PEARL, EGG)
    Projectiles that should NOT trigger combat tagging.

quitPunishmentEventPriority  (EventPriority, default: NORMAL)
    Bukkit event priority for quit punishment handler.

whitelistedKickReasons  (list of strings)
    Kick reasons that will NOT trigger combat punishment (logout).

----------------------------------------------------------------
15. INVENTORY SETTINGS
----------------------------------------------------------------
inventoryAccessMode  (enum: ALLOW_ALL | RESTRICT, default: ALLOW_ALL)
    ALLOW_ALL - players can open any inventory during combat.
    RESTRICT  - certain inventory types are blocked during combat.

restrictedInventoryTypes  (list of InventoryType)
    Inventory types blocked when in RESTRICT mode.
    E.g. CHEST, ENDER_CHEST, BARREL, SHULKER_BOX.

----------------------------------------------------------------
16. PLACEHOLDER SETTINGS
----------------------------------------------------------------
isInCombatFormattedTrue  (string, default: "In Combat")
    Placeholder output when the player is in combat.

isInCombatFormattedFalse  (string, default: "Not In Combat")
    Placeholder output when the player is not in combat.

----------------------------------------------------------------
17. MESSAGES SETTINGS
----------------------------------------------------------------
Each message is a Notice (MiniMessage formatted).
Available placeholders vary per message, most support {PLAYER},
{TIME}, {PERMISSION}, {USAGE}, {Y}, {MODE}, {COUNT}.

Admin sub-messages:
  onlyForPlayers, adminTagPlayer, adminTagMultiplePlayers,
  adminUntagPlayer, adminUntagAll, adminPlayerNotInCombat,
  playerInCombat, playerNotInCombat, adminCannotTagSelf,
  adminTagOutSelf, adminTagOut, playerTagOut,
  adminTagOutOff, playerTagOutOff, adminTagOutCanceled,
  combatStats

----------------------------------------------------------------
18. LIFESTEAL SETTINGS
----------------------------------------------------------------
enabled  (boolean, default: false)
    Master toggle for LifestealCore integration.
    Requires the LifestealCore plugin to be installed on the server.

heartsToSteal  (int, default: 1)
    Number of hearts to transfer from victim to killer on death.

stealOnlyInCombat  (boolean, default: true)
    When true, hearts are only stolen if either the killer or victim
    is in combat (controlled by stealOnKill / stealOnDeath).
    When false, hearts are stolen on any kill regardless of combat state.

stealOnKill  (boolean, default: true)
    The killer steals hearts from the victim on kill.

stealOnDeath  (boolean, default: false)
    The victim loses hearts on death even if the killer does not gain them.
    This is additive with stealOnKill.

eliminateOnZeroHearts  (boolean, default: true)
    When the victim's hearts would drop to zero or below, they are
    marked as eliminated (banished) by LifestealCore.

notifyOnKill  (boolean, default: true)
    Show a chat notification when hearts are stolen.

----------------------------------------------------------------
How to configure
----------------------------------------------------------------
1. Place config.yml in /plugins/Dcombat/
2. Restart the server or run /dcombat reload
3. Keep wiki.txt in the same folder as a reference guide.
""";

    private WikiGenerator() {
    }

    public static void writeWiki(File dataFolder) {
        if (dataFolder == null) {
            return;
        }

        Path wikiPath = dataFolder.toPath().resolve(FILE_NAME);
        try {
            if (!dataFolder.exists()) {
                Files.createDirectories(dataFolder.toPath());
            }
            Files.writeString(wikiPath, CONTENT, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + FILE_NAME + " to " + dataFolder, e);
        }
    }
}
