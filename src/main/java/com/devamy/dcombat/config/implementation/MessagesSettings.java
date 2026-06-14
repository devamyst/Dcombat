package com.devamy.dcombat.config.implementation;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
public class MessagesSettings extends OkaeriConfig {

    public AdminMessages admin = new AdminMessages();

    public Notice combatNotification = Notice.builder()
        .actionBar("Combat ends in: <red>{TIME}</red>")
        .build();

    public boolean withoutMillis = true;

    public String unknownPlayerPlaceholder = "Unknown";

    public Notice noPermission = Notice.chat(
        "<gradient:red:dark_red>You don't have permission <dark_gray>(<gray>{PERMISSION}</gray>)</dark_gray> to perform this command!</gradient>");

    public Notice playerNotFound = Notice
        .chat("<gradient:#ff0000:#ff6b6b>The specified player could not be found!</gradient>");

    public Notice playerTagged = Notice.chat(
        "<gradient:red:yellow>⚠ <white>You are in combat!</white> <u>Do not leave the server!</gradient>");

    public Notice playerUntagged = Notice.chat(
        "<gradient:#00ff00:#00b300>✌ <white>Combat ended!</white> You can now safely leave!</gradient>");

    public Notice playerLoggedOutDuringCombat = Notice
        .chat("<gradient:red:dark_red>⚠ <white>{PLAYER}</white> logged off during combat!</gradient>");

    public Notice commandDisabledDuringCombat = Notice.chat(
        "<gradient:red:yellow>⚠ <white>Command blocked!</white> Cannot use this during combat!</gradient>");

    public Notice elytraDisabledDuringCombat = Notice.chat(
        "<gradient:red:yellow>⚠ <white>Elytra disabled!</white> Cannot use elytra during combat!</gradient>"
    );

    public Notice invalidCommandUsage = Notice.chat("<gray>Usage: <gradient:gold:yellow>{USAGE}</gradient>");

    public Notice inventoryBlockedDuringCombat = Notice.chat(
        "<gradient:red:dark_red>⚠ <white>Inventory access</white> is restricted during combat!</gradient>");

    public Notice blockPlacingBlockedDuringCombat = Notice
        .chat("<gradient:red:yellow>⚠ Block placement <white>{MODE} Y:{Y}</white> is restricted!</gradient>");

    public Notice fireworksDisabled = Notice
        .actionbar("<gradient:red:yellow>⚠ You can't use fireworks during combat ⚠</gradient>");

    public Notice cantEnterOnRegion = Notice.chat(
        "<gradient:red:dark_red>⚠ Restricted area!</gradient> <white>Cannot enter during combat!</white>");

    public String lifestealHeartsStolen = "<gradient:red:gold>❤ <white>{PLAYER}</white> lost <red>{HEARTS}</red> hearts to <white>{KILLER}</white>!</gradient>";

    public static class AdminMessages extends OkaeriConfig {
        public Notice onlyForPlayers = Notice.chat("<gradient:red:dark_red>❌ This command is player-only!</gradient>");

        public Notice adminTagPlayer = Notice
            .chat("<gradient:#00b3ff:#0066ff>⚔ <gray>Tagged player:</gray> <white>{PLAYER}</white></gradient>");

        public Notice adminTagMultiplePlayers = Notice.chat(
            "<gradient:#00b3ff:#0066ff>⚔ <gray>Tagged:</gray> <white>{FIRST_PLAYER}</white> <gray>and</gray> <white>{SECOND_PLAYER}</white></gradient>");

        public Notice adminUntagPlayer = Notice.chat(
            "<gradient:#00ff88:#00b300>✌ <gray>Removed</gray> <white>{PLAYER}</white> <gray>from combat</gray></gradient>");

        public Notice adminUntagAll = Notice.chat(
            "<gradient:#00ff88:#00b300>✌ <gray>Removed</gray> <white>{COUNT}</white> <gray> players from combat</gray></gradient>");

        public Notice adminPlayerNotInCombat = Notice
            .chat("<gradient:red:dark_red>❌ <white>{PLAYER}</white> is not in combat!</gradient>");

        public Notice playerInCombat = Notice
            .chat("<gradient:#ff6666:#ff0000>⚔ <white>{PLAYER}</white> <gray>is in combat!</gray></gradient>");

        public Notice playerNotInCombat = Notice
            .chat("<gradient:#00ff00:#00b300>✌ <white>{PLAYER}</white> <gray>is safe</gray></gradient>");

        public Notice adminCannotTagSelf = Notice.chat("<gradient:red:dark_red>❌ Cannot tag yourself!</gradient>");

        public Notice adminTagOutSelf = Notice.chat(
            "<gradient:#00b3ff:#0066ff>🛡 <gray>Self-protection active for</gray> <white>{TIME}</white></gradient>");

        public Notice adminTagOut = Notice.chat(
            "<gradient:#00b3ff:#0066ff>🛡 <gray>Protected</gray> <white>{PLAYER}</white> <gray>for</gray> <white>{TIME}</white></gradient>");

        public Notice playerTagOut = Notice.chat(
            "<gradient:#00ff88:#00b300>🛡 <gray>Protection active for</gray> <white>{TIME}</white></gradient>");

        public Notice adminTagOutOff = Notice.chat(
            "<gradient:#00ff88:#00b300>✌ <gray>Re-enabled tagging for</gray> <white>{PLAYER}</white></gradient>");

        public Notice playerTagOutOff = Notice.chat("");

        public Notice adminTagOutCanceled = Notice
            .chat("<gradient:red:dark_red>❌ Player has tag-out protection!</gradient>");

        public Notice combatStats = Notice
            .chat("<gradient:#ff6666:#ff0000>⚔ <gray>Players in combat:</gray> <white>{COUNT}</white></gradient>");
    }
}
