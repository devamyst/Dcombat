package com.devamy.dcombat.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;

public final class StartupBanner {

    private static final String GRADIENT_FROM = "#8a1212";
    private static final String GRADIENT_TO = "#fc6b03";

    private static final String[] LOGO = {
        " _____      _             _ _           ",
        "|  __ \\    | |           | (_)          ",
        "| |  | |___| |_ _   _  __| |_  ___  ___ ",
        "| |  | / __| __| | | |/ _` | |/ _ \\/ __|",
        "| |__| \\__ \\ |_| |_| | (_| | | (_) \\__ \\",
        "|_____/|___/\\__|\\__,_|\\__,_|_|\\___/|___/"
    };

    private StartupBanner() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }

    public static void print(Server server, MiniMessage miniMessage, PluginDescriptionFile description, boolean folia) {
        for (String line : LOGO) {
            send(server, miniMessage, gradient(line));
        }

        send(server, miniMessage, "  <gray>" + description.getName()
            + " <white>v" + description.getVersion() + "</white> by " + gradient("Dstudios"));
        send(server, miniMessage, "  <gray>Platform: <white>" + (folia ? "Folia" : "Paper") + "</white>");
        server.getConsoleSender().sendMessage(Component.empty());
    }

    public static void printReady(Server server, MiniMessage miniMessage, long millis) {
        send(server, miniMessage, "<gradient:" + GRADIENT_FROM + ":" + GRADIENT_TO + ">✔</gradient> "
            + "<gray>Enabled successfully in <white>" + millis + "ms</white>");
    }

    private static String gradient(String text) {
        return "<gradient:" + GRADIENT_FROM + ":" + GRADIENT_TO + ">" + text + "</gradient>";
    }

    private static void send(Server server, MiniMessage miniMessage, String miniMessageText) {
        server.getConsoleSender().sendMessage(miniMessage.deserialize(miniMessageText));
    }
}
