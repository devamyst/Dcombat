# Dcombat

A feature-rich combat plugin for **Paper** and **Folia** Minecraft servers. Dcombat handles combat tagging, logout punishment, region-aware behavior, visual combat borders, drop control, and deep integration with common server plugins.

---

## Table of Contents

1. [Requirements](#requirements)
2. [Installation](#installation)
3. [Commands & Permissions](#commands--permissions)
4. [Configuration Reference](#configuration-reference)
   - [Settings](#1-settings)
   - [Pearl Settings](#2-pearl-settings)
   - [Trident Settings](#3-trident-settings)
   - [Effect Settings](#4-effect-settings)
   - [Death Settings](#5-death-settings)
   - [Drop Settings](#6-drop-settings)
   - [Knockback Settings](#7-knockback-settings)
   - [Border Settings](#8-border-settings)
   - [Block Placement Settings](#9-block-placement-settings)
   - [Crystal PvP Settings](#10-crystal-pvp-settings)
   - [Command Settings](#11-command-settings)
   - [Admin Settings](#12-admin-settings)
   - [Region Settings](#13-region-settings)
   - [Combat Settings](#14-combat-settings)
   - [Inventory Settings](#15-inventory-settings)
   - [Placeholder Settings](#16-placeholder-settings)
   - [Messages Settings](#17-messages-settings)
   - [Lifesteal Settings](#18-lifesteal-settings)
5. [PlaceholderAPI](#placeholderapi)
6. [Developer API](#developer-api)
7. [Plugin Integrations](#plugin-integrations)
8. [Building from Source](#building-from-source)
9. [Repository Layout](#repository-layout)
10. [GitHub Workflow](#github-workflow)
12. [License](#license)

---

## Requirements

| Requirement | Version |
|---|---|
| Java | 21+ |
| Maven | 3.9+ (build only) |
| Paper / Folia | API 1.19 or newer |
| [PacketEvents](https://www.spigotmc.org/resources/packetevents-api.80279/) | Required — must be installed on the server |

**Optional integrations** (soft dependencies — the plugin works without them):

- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- [WorldGuard](https://enginehub.org/worldguard)
- [Lands](https://www.spigotmc.org/resources/lands.53313/)
- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- [LifestealCore](https://www.spigotmc.org/resources/1-8-8-26-1-2-lifestealcore-the-premium-lifesteal-experience-now-with-custom-textures.101284/)

---

## Installation

1. Build the plugin (see [Building from Source](#building-from-source)) or download a pre-built jar from the GitHub Releases page.
2. Copy the jar into your server's `plugins/` folder.
3. Install **PacketEvents** and any optional integration plugins you want to use.
4. Start (or restart) the server. Dcombat will generate `plugins/Dcombat/config.yml` and a `wiki.txt` reference file.
5. Edit `config.yml` to suit your server.
6. Run `/dcombat reload` to apply changes without a full restart.

---

## Commands & Permissions

All admin commands share the root alias `combatlog` or `combat`.

### `/combatlog reload`
Reloads the plugin configuration.

| Permission | `dcombat.reload` |
|---|---|

---

### `/combatlog status <player>`
Shows whether a player is currently combat-tagged.

| Permission | `dcombat.status` |
|---|---|

---

### `/combatlog tag <player>`
Manually combat-tags a single player for the configured timer duration.

| Permission | `dcombat.tag` |
|---|---|

### `/combatlog tag <player1> <player2>`
Mutually tags two players against each other. The sender cannot be one of the targets.

| Permission | `dcombat.tag` |
|---|---|

---

### `/combatlog untag <player>`
Removes the combat tag from a player immediately.

| Permission | `dcombat.untag` |
|---|---|

---

### `/combatlog untagall`
Removes the combat tag from every currently tagged player.

| Permission | `dcombat.untagall` |
|---|---|

---

### `/combatlog stats`
Displays how many players are currently in combat.

| Permission | `dcombat.stats` |
|---|---|

---

### `/tagout <duration>` (alias: `/tagimmunity`)
Grants the sender temporary tag-out immunity for the given duration. While tagged out, the player cannot be combat-tagged.

| Permission | `dcombat.tagout` |
|---|---|

### `/tagout <player> <duration>`
Grants another player tag-out immunity for the given duration.

| Permission | `dcombat.tagout` |
|---|---|

### `/tagout remove [player]`
Removes tag-out immunity from yourself or the specified player.

| Permission | `dcombat.tagout` |
|---|---|

---

### Bypass Permission
| Permission | Effect |
|---|---|
| `dcombat.bypass.admin` | Prevents the player from ever being combat-tagged (requires `excludeAdminsFromCombat: true` in config) |

---

## Configuration Reference

A `wiki.txt` file is also written to the plugin's data folder as an in-server quick reference. The sections below match the keys in `config.yml`.

---

### 1. Settings

```yaml
settings:
  notifyAboutUpdates: true
  combatTimerDuration: 20s
  ignoredWorlds:
    - your_world
```

| Key | Type | Default | Description |
|---|---|---|---|
| `notifyAboutUpdates` | boolean | `true` | Notify server operators when a new plugin version is available. |
| `combatTimerDuration` | Duration | `20s` | How long a player stays in combat after their last PvP interaction. Format: `15s`, `1m`, `1h`. |
| `ignoredWorlds` | list | `[your_world]` | Worlds where combat tagging is completely disabled. |

---

### 2. Pearl Settings

```yaml
pearl:
  pearlThrowDamageEnabled: true
  pearlThrowDisabledDuringCombat: true
  pearlCooldownEnabled: false
  pearlExtendsCombatTag: false
  pearlThrowDelay: 3s
```

| Key | Type | Default | Description |
|---|---|---|---|
| `pearlThrowDamageEnabled` | boolean | `true` | Whether ender pearl landing deals damage to the thrower. |
| `pearlThrowDisabledDuringCombat` | boolean | `true` | Block pearl throws while in combat. |
| `pearlCooldownEnabled` | boolean | `false` | Enable a cooldown between pearl throws. |
| `pearlExtendsCombatTag` | boolean | `false` | Throwing a pearl during combat resets the combat timer. |
| `pearlThrowDelay` | Duration | `3s` | Cooldown length between pearl throws (only applies when `pearlCooldownEnabled: true`). |

---

### 3. Trident Settings

```yaml
trident:
  tridentRiptideDisabledDuringCombat: false
  tridentRiptideExtendsCombatTag: false
  tridentRiptideDelay: 10s
```

| Key | Type | Default | Description |
|---|---|---|---|
| `tridentRiptideDisabledDuringCombat` | boolean | `false` | Block riptide while in combat. |
| `tridentRiptideExtendsCombatTag` | boolean | `false` | Using riptide during combat resets the combat timer. |
| `tridentRiptideDelay` | Duration | `10s` | Cooldown between riptide uses. |

---

### 4. Effect Settings

```yaml
effect:
  customEffectsEnabled: false
  customEffects:
    SPEED: 1
    RESISTANCE: 0
```

| Key | Type | Default | Description |
|---|---|---|---|
| `customEffectsEnabled` | boolean | `false` | Apply potion effects to players while they are in combat. |
| `customEffects` | map | `SPEED:1, RESISTANCE:0` | Map of `PotionEffectType → amplifier`. Amplifier `0` = Level I, `1` = Level II, etc. |

---

### 5. Death Settings

Controls visual effects that trigger on player death.

```yaml
death:
  lightning:
    afterEveryDeath: false
    inCombat: true
  firework:
    afterEveryDeath: false
    inCombat: false
    power: 2
    fireworkType: BALL
    primaryColor: "#a80022"
    fadeColor: "#0a0a0a"
    particlesEnabled: true
    mainParticle: CAMPFIRE_COSY_SMOKE
    mainParticleCount: 3
    secondaryParticle: SMALL_FLAME
    secondaryParticleCount: 3
```

**Lightning**

| Key | Type | Default | Description |
|---|---|---|---|
| `lightning.afterEveryDeath` | boolean | `false` | Strike lightning on every player death. |
| `lightning.inCombat` | boolean | `true` | Strike lightning only on deaths during active combat. |

**Firework**

| Key | Type | Default | Description |
|---|---|---|---|
| `firework.afterEveryDeath` | boolean | `false` | Spawn a firework on every player death. |
| `firework.inCombat` | boolean | `false` | Spawn a firework only on combat deaths. |
| `firework.power` | int | `2` | Firework flight power / duration. |
| `firework.fireworkType` | FireworkEffect.Type | `BALL` | Shape: `BALL`, `BALL_LARGE`, `STAR`, `CREEPER`, `BURST`. |
| `firework.primaryColor` | hex string | `#a80022` | Primary explosion color. |
| `firework.fadeColor` | hex string | `#0a0a0a` | Fade-out color. |
| `firework.particlesEnabled` | boolean | `true` | Emit ambient particles alongside the firework. |
| `firework.mainParticle` | XParticle | `CAMPFIRE_COSY_SMOKE` | Primary particle type. |
| `firework.mainParticleCount` | int | `3` | Number of primary particles per tick. |
| `firework.secondaryParticle` | XParticle | `SMALL_FLAME` | Secondary particle type. |
| `firework.secondaryParticleCount` | int | `3` | Number of secondary particles per tick. |

---

### 6. Drop Settings

Controls how a player's inventory is dropped when they die during combat.

```yaml
drop:
  dropEventPriority: NORMAL
  dropType: UNCHANGED
  dropItemPercent: 100
  playersHealthPercentClamp: 20
  affectExperience: false
```

| Key | Type | Default | Description |
|---|---|---|---|
| `dropEventPriority` | EventPriority | `NORMAL` | Bukkit event priority for the drop handler. |
| `dropType` | enum | `UNCHANGED` | Drop behavior mode (see below). |
| `dropItemPercent` | int | `100` | Percentage of items the victim **keeps** when `dropType: PERCENT`. Lower values mean more drops lost. |
| `playersHealthPercentClamp` | int | `20` | Minimum percentage of items always kept when `dropType: PLAYERS_HEALTH`. Prevents total inventory loss. |
| `affectExperience` | boolean | `false` | Apply the drop modifier to experience orbs as well. |

**`dropType` values:**

| Value | Behavior |
|---|---|
| `UNCHANGED` | Vanilla drop behavior — no modification. |
| `PERCENT` | A fixed percentage of items are dropped, controlled by `dropItemPercent`. |
| `PLAYERS_HEALTH` | Drop percentage scales with the victim's remaining health at time of death. Clamped by `playersHealthPercentClamp`. |

---

### 7. Knockback Settings

```yaml
knockback:
  multiplier: 1.0
  vertical: 0.2
  forceTeleport:
    delay: 1s
    unsafeGroundBlocks: []
    airBlocks: []
```

| Key | Type | Default | Description |
|---|---|---|---|
| `multiplier` | double | `1.0` | Global knockback strength multiplier. |
| `vertical` | double | `0.2` | Vertical knockback component. |
| `forceTeleport.delay` | Duration | `1s` | Time before a safe-teleport check fires after knockback sends a player near an unsafe area. |
| `forceTeleport.unsafeGroundBlocks` | list of XMaterial | `[]` | Block types considered unsafe to land on (triggers safe-teleport). |
| `forceTeleport.airBlocks` | list of XMaterial | `[]` | Block types treated as air (also triggers safe-teleport). |

---

### 8. Border Settings

A visual combat border rendered around the player during combat.

```yaml
border:
  distance: 6.5
  block:
    # see border/animation/block package for sub-keys
  particle:
    # see border/animation/particle package for sub-keys
```

| Key | Type | Default | Description |
|---|---|---|---|
| `distance` | double | `6.5` | Distance from the player at which the border is rendered. |
| `block` | section | — | Configuration for the block-based border animation. |
| `particle` | section | — | Configuration for the particle-based border animation. |

---

### 9. Block Placement Settings

```yaml
blockPlacement:
  disableBlockPlacing: true
  blockPlacementMode: ABOVE
  blockPlacementModeDisplayName: "above"
  blockPlacementYCoordinate: 40
  restrictedBlockTypes: []
```

| Key | Type | Default | Description |
|---|---|---|---|
| `disableBlockPlacing` | boolean | `true` | Prevent block placement while in combat. |
| `blockPlacementMode` | enum | `ABOVE` | `ABOVE` — cannot place blocks above the Y limit. `BELOW` — cannot place blocks below the Y limit. |
| `blockPlacementModeDisplayName` | string | `above` | Display name used in player-facing messages for the current mode. |
| `blockPlacementYCoordinate` | int | `40` | The Y-level used as the boundary. |
| `restrictedBlockTypes` | list of Material | `[]` | Specific block materials that can never be placed during combat, regardless of Y level. |

---

### 10. Crystal PvP Settings

```yaml
crystalPvp:
  tagFromCrystals: true
  tagFromRespawnAnchor: true
```

| Key | Type | Default | Description |
|---|---|---|---|
| `tagFromCrystals` | boolean | `true` | End crystal explosions combat-tag the player responsible. |
| `tagFromRespawnAnchor` | boolean | `true` | Respawn anchor explosions combat-tag the player responsible. |

---

### 11. Command Settings

```yaml
command:
  commandRestrictionMode: BLACKLIST
  restrictedCommands:
    - spawn
    - home
```

| Key | Type | Default | Description |
|---|---|---|---|
| `commandRestrictionMode` | enum | `BLACKLIST` | `BLACKLIST` — all commands are allowed except those listed. `WHITELIST` — only listed commands are allowed. |
| `restrictedCommands` | list | `[]` | Commands affected by the restriction mode. Do **not** include the leading `/`. |

---

### 12. Admin Settings

```yaml
admin:
  excludeAdminsFromCombat: false
  excludeCreativePlayersFromCombat: false
```

| Key | Type | Default | Description |
|---|---|---|---|
| `excludeAdminsFromCombat` | boolean | `false` | Players with `dcombat.bypass.admin` are never tagged. |
| `excludeCreativePlayersFromCombat` | boolean | `false` | Players in Creative mode are never tagged. |

---

### 13. Region Settings

Requires **WorldGuard** to be installed.

```yaml
region:
  blockedRegions:
    - your_region
  preventPvpInRegions: true
  restrictedRegionRadius: 10
```

| Key | Type | Default | Description |
|---|---|---|---|
| `blockedRegions` | list | `[your_region]` | WorldGuard region names where combat tagging is blocked. |
| `preventPvpInRegions` | boolean | `true` | Prevent PvP from starting inside blocked regions. |
| `restrictedRegionRadius` | int | `10` | Radius (in blocks) around blocked regions where combat is also prevented. |

---

### 14. Combat Settings

```yaml
combat:
  releaseAttackerOnVictimDeath: true
  disableElytraUsage: true
  disableElytraOnDamage: true
  disableFlying: true
  unequipElytraOnCombat: true
  disableFireworks: true
  enableDamageCauseLogging: false
  damageCauseRestrictionMode: WHITELIST
  loggedDamageCauses: []
  ignoredProjectileTypes:
    - ENDER_PEARL
    - EGG
  quitPunishmentEventPriority: NORMAL
  whitelistedKickReasons: []
```

| Key | Type | Default | Description |
|---|---|---|---|
| `releaseAttackerOnVictimDeath` | boolean | `true` | When the victim dies, the attacker is also untagged. |
| `disableElytraUsage` | boolean | `true` | Prevent elytra use entirely while in combat. |
| `disableElytraOnDamage` | boolean | `true` | Cancel elytra glide when the player takes damage. |
| `disableFlying` | boolean | `true` | Disable `/fly` mode while in combat. |
| `unequipElytraOnCombat` | boolean | `true` | Force-unequip the chestplate slot on combat entry (removes equipped elytra). |
| `disableFireworks` | boolean | `true` | Block firework rocket usage during combat. |
| `enableDamageCauseLogging` | boolean | `false` | Log every damage cause to console — useful for debugging what triggers tagging. |
| `damageCauseRestrictionMode` | enum | `WHITELIST` | `WHITELIST` — only listed causes trigger combat. `BLACKLIST` — all causes except listed trigger combat. |
| `loggedDamageCauses` | list of DamageCause | `[]` | Damage causes affected by the restriction mode. |
| `ignoredProjectileTypes` | list of EntityType | `ENDER_PEARL, EGG` | Projectile types that do **not** trigger a combat tag. |
| `quitPunishmentEventPriority` | EventPriority | `NORMAL` | Event priority for the logout-punishment handler. |
| `whitelistedKickReasons` | list | `[]` | Server kick messages that will **not** be treated as a combat logout. |

---

### 15. Inventory Settings

```yaml
inventory:
  inventoryAccessMode: ALLOW_ALL
  restrictedInventoryTypes:
    - CHEST
    - ENDER_CHEST
```

| Key | Type | Default | Description |
|---|---|---|---|
| `inventoryAccessMode` | enum | `ALLOW_ALL` | `ALLOW_ALL` — players can open any container. `RESTRICT` — certain container types are blocked. |
| `restrictedInventoryTypes` | list of InventoryType | `[]` | Inventory types blocked in `RESTRICT` mode (e.g. `CHEST`, `ENDER_CHEST`, `BARREL`, `SHULKER_BOX`). |

---

### 16. Placeholder Settings

```yaml
placeholders:
  isInCombatFormattedTrue: "In Combat"
  isInCombatFormattedFalse: "Not In Combat"
```

| Key | Type | Default | Description |
|---|---|---|---|
| `isInCombatFormattedTrue` | string | `In Combat` | Value returned by `%dcombat_isInCombat_formatted%` when the player is tagged. |
| `isInCombatFormattedFalse` | string | `Not In Combat` | Value returned by `%dcombat_isInCombat_formatted%` when the player is not tagged. |

---

### 17. Messages Settings

All messages use [MiniMessage](https://docs.advntr.dev/minimessage/format.html) formatting. Each message is a `Notice` object that can target the chat, action bar, title, or sound.

Available placeholders vary per message — the most common ones are listed below:

| Placeholder | Description |
|---|---|
| `{PLAYER}` | Target player's name |
| `{TIME}` | Duration value |
| `{PERMISSION}` | Missing permission node |
| `{USAGE}` | Correct command usage |
| `{Y}` | Y-level coordinate |
| `{MODE}` | Block placement mode name |
| `{COUNT}` | Number of players |
| `{FIRST_PLAYER}` | First player in a dual-tag |
| `{SECOND_PLAYER}` | Second player in a dual-tag |

**Admin message keys:**

| Key | When sent |
|---|---|
| `onlyForPlayers` | Command used from console when player-only |
| `adminTagPlayer` | Admin tags a single player |
| `adminTagMultiplePlayers` | Admin tags two players |
| `adminUntagPlayer` | Admin untags a player |
| `adminUntagAll` | Admin untags everyone |
| `adminPlayerNotInCombat` | Target was not in combat |
| `playerInCombat` | Status check — player is tagged |
| `playerNotInCombat` | Status check — player is not tagged |
| `adminCannotTagSelf` | Admin tried to tag themselves |
| `adminTagOutSelf` | Admin granted themselves tag-out immunity |
| `adminTagOut` | Admin granted a player tag-out immunity |
| `playerTagOut` | Sent to the player who received immunity |
| `adminTagOutOff` | Admin removed a player's tag-out immunity |
| `playerTagOutOff` | Sent to the player whose immunity was removed |
| `adminTagOutCanceled` | Tag command blocked because player is tagged out |
| `combatStats` | Response to `/combatlog stats` |

---

### 18. Lifesteal Settings

Requires [LifestealCore](https://www.spigotmc.org/resources/1-8-8-26-1-2-lifestealcore-the-premium-lifesteal-experience-now-with-custom-textures.101284/) to be installed.

```yaml
lifesteal:
  enabled: false
  heartsToSteal: 1
  stealOnlyInCombat: true
  stealOnKill: true
  stealOnDeath: false
  eliminateOnZeroHearts: true
  notifyOnKill: true
```

| Key | Type | Default | Description |
|---|---|---|---|
| `enabled` | boolean | `false` | Master toggle for LifestealCore integration. |
| `heartsToSteal` | int | `1` | Hearts transferred from victim to killer on death. |
| `stealOnlyInCombat` | boolean | `true` | Only steal hearts when at least one party is combat-tagged. |
| `stealOnKill` | boolean | `true` | Killer gains hearts on kill. |
| `stealOnDeath` | boolean | `false` | Victim loses hearts on death independently of `stealOnKill`. |
| `eliminateOnZeroHearts` | boolean | `true` | When a victim's hearts drop to zero, LifestealCore eliminates (bans) them. |
| `notifyOnKill` | boolean | `true` | Send a chat notification when hearts are stolen. |

---

## PlaceholderAPI

Install **PlaceholderAPI** to use these placeholders. The identifier is `dcombat`.

| Placeholder | Returns |
|---|---|
| `%dcombat_isInCombat%` | `true` or `false` |
| `%dcombat_isInCombat_formatted%` | Configured `isInCombatFormattedTrue` / `isInCombatFormattedFalse` strings |
| `%dcombat_remaining_seconds%` | Seconds remaining on the combat timer (empty string when not tagged) |
| `%dcombat_remaining_millis%` | Milliseconds remaining on the combat timer (empty string when not tagged) |
| `%dcombat_opponent%` | Name of the player who tagged this player (empty string when not tagged) |
| `%dcombat_opponent_health%` | Health of the opponent, formatted to 2 decimal places (empty string when not tagged) |

---

## Developer API

Dcombat exposes a `DcombatApi` interface for other plugins to hook into.

```java
DcombatApi api = DcombatProvider.get(); // static accessor

FightManager    fightManager    = api.getFightManager();
RegionProvider  regionProvider  = api.getRegionProvider();
PearlService    pearlService    = api.getFightPearlService();
FightTagOutService tagOutService = api.getFightTagOutService();
FightEffectService effectService = api.getFightEffectService();
DropService     dropService     = api.getDropService();
DropKeepInventoryService keepInv = api.getDropKeepInventoryService();
```

### Bukkit Events

Listen to these events in your plugin to react to combat state changes.

#### `FightTagEvent`
Fired when a player is combat-tagged. Cancellable.

```java
@EventHandler
public void onTag(FightTagEvent event) {
    UUID player = event.getPlayer();
    CauseOfTag cause = event.getCause(); // PVP, COMMAND, etc.
    event.setCancelled(true); // prevent tagging
}
```

#### `FightUntagEvent`
Fired when a player's combat tag is removed. Cancellable.

```java
@EventHandler
public void onUntag(FightUntagEvent event) {
    UUID player = event.getPlayer();
    CauseOfUnTag cause = event.getCause(); // DEATH, TIMER, COMMAND, etc.
}
```

#### `BorderShowAsyncEvent` / `BorderHideAsyncEvent`
Fired asynchronously when the visual combat border is shown or hidden for a player.

---

## Plugin Integrations

| Plugin | Behavior when installed |
|---|---|
| **PacketEvents** | **Required.** Used for packet-level elytra, flying, and border rendering. |
| **PlaceholderAPI** | Registers the `%dcombat_*%` placeholder expansion automatically. |
| **WorldGuard** | Enables `blockedRegions` and `restrictedRegionRadius` functionality. |
| **Lands** | Uses Lands claim boundaries as combat-safe regions. |
| **Vault** | Economy hook (used by integrations that may depend on it). |
| **LifestealCore** | Enables heart-stealing on combat kills when configured. |

---

## Building from Source

```bash
git clone https://github.com/Devamy/Dcombat-Minecraft.git
cd Dcombat-Minecraft
mvn clean package
```

The compiled jar is output to `target/`. Copy it to your server's `plugins/` folder.

---

## Repository Layout

```
src/main/java/       Plugin source code
src/main/resources/  plugin.yml and bundled resources
pom.xml              Maven build descriptor
.github/workflows/   CI build definitions
```

The following are intentionally **not** tracked:

- `target/` — Maven build output
- `*.jar` — compiled plugin artifacts
- IDE project files (`.idea/`, `.classpath`, etc.)

---

## GitHub Workflow

Every push and pull request triggers a Maven build using Java 21. See `.github/workflows/build.yml`.

---
---

## License

Copyright (c) 2026 Devamy. All rights reserved.

This project is proprietary source code. No permission is granted to copy, modify, distribute, sell, sublicense, or claim this code as someone else's work without prior written permission from the copyright holder.