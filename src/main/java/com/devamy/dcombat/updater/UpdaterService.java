package com.devamy.dcombat.updater;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.bukkit.plugin.PluginDescriptionFile;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdaterService {

    private static final String MODRINTH_PROJECT_ID = "Dcombat";
    private static final String CACHE_KEY = "modrinth-update";
    private static final Pattern VERSION_NUMBER_PATTERN = Pattern.compile("\"version_number\"\\s*:\\s*\"([^\"]+)\"");

    private final AsyncLoadingCache<String, UpdateResult> updateCache;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String currentVersion;

    public UpdaterService(PluginDescriptionFile descriptionFile) {
        this.currentVersion = descriptionFile.getVersion();

        this.updateCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .buildAsync(key -> this.checkModrinth());
    }

    CompletableFuture<UpdateResult> checkForUpdate() {
        return this.updateCache.get(CACHE_KEY);
    }

    private UpdateResult checkModrinth() throws IOException, InterruptedException {
        URI uri = URI.create("https://api.modrinth.com/v2/project/" + URLEncoder.encode(MODRINTH_PROJECT_ID, StandardCharsets.UTF_8) + "/version");
        HttpRequest request = HttpRequest.newBuilder(uri)
            .header("User-Agent", "Dcombat update checker")
            .GET()
            .build();

        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            return new UpdateResult(false);
        }

        Matcher matcher = VERSION_NUMBER_PATTERN.matcher(response.body());

        while (matcher.find()) {
            String latestVersion = matcher.group(1);

            if (this.isNewerVersion(latestVersion, this.currentVersion)) {
                return new UpdateResult(true, latestVersion);
            }
        }

        return new UpdateResult(false);
    }

    private boolean isNewerVersion(String latest, String current) {
        if (latest.equalsIgnoreCase(current)) {
            return false;
        }

        String[] latestParts = latest.split("[._-]");
        String[] currentParts = current.split("[._-]");

        int maxLength = Math.max(latestParts.length, currentParts.length);

        for (int i = 0; i < maxLength; i++) {
            int latestPart = i < latestParts.length ? this.parseVersionPart(latestParts[i]) : 0;
            int currentPart = i < currentParts.length ? this.parseVersionPart(currentParts[i]) : 0;

            if (latestPart != currentPart) {
                return latestPart > currentPart;
            }
        }

        return !latest.equalsIgnoreCase(current);
    }

    private int parseVersionPart(String part) {
        try {
            return Integer.parseInt(part);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
