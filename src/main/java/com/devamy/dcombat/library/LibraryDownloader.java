package com.devamy.dcombat.library;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LibraryDownloader {

    private static final int CONNECT_TIMEOUT = 10_000;
    private static final int READ_TIMEOUT = 30_000;

    private final Path libsDirectory;
    private final Logger logger;
    private final List<LibraryDefinition> libraries;
    private final List<Path> downloadedJars = new ArrayList<>();

    public LibraryDownloader(Path dataFolder, Logger logger, List<LibraryDefinition> libraries) {
        this.libsDirectory = dataFolder.resolve("libs");
        this.logger = logger;
        this.libraries = libraries;
    }

    public void downloadAll() {
        try {
            Files.createDirectories(this.libsDirectory);
        } catch (IOException e) {
            this.logger.warning("Could not create libs directory: " + e.getMessage());
            return;
        }

        for (LibraryDefinition lib : this.libraries) {
            this.downloadIfMissing(lib);
        }
    }

    private void downloadIfMissing(LibraryDefinition lib) {
        Path jarPath = this.libsDirectory.resolve(lib.jarFileName());

        if (Files.exists(jarPath)) {
            this.downloadedJars.add(jarPath);
            return;
        }

        this.logger.info("Downloading library: " + lib.artifactId() + " v" + lib.version() + "...");

        try {
            URL url = new URI(lib.mavenUrl()).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("User-Agent", "Dcombat-LibraryDownloader");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                this.logger.warning("Failed to download " + lib.artifactId() + ": HTTP " + responseCode);
                return;
            }

            long contentLength = conn.getContentLengthLong();
            try (InputStream in = conn.getInputStream()) {
                Files.copy(in, jarPath);
            }

            long actualSize = Files.size(jarPath);
            if (contentLength > 0 && actualSize != contentLength) {
                this.logger.warning("Downloaded " + lib.artifactId() + " size mismatch (expected " + contentLength + ", got " + actualSize + ")");
                Files.delete(jarPath);
                return;
            }

            this.downloadedJars.add(jarPath);
            this.logger.info("Downloaded " + lib.artifactId() + " v" + lib.version() + " (" + this.formatSize(actualSize) + ")");
        } catch (IOException | URISyntaxException e) {
            this.logger.warning("Could not download " + lib.artifactId() + ": " + e.getMessage());
        }
    }

    public boolean injectClasspath(ClassLoader pluginClassLoader) {
        if (this.downloadedJars.isEmpty()) {
            return true;
        }

        if (!(pluginClassLoader instanceof URLClassLoader urlClassLoader)) {
            this.logger.warning("Plugin classloader is not a URLClassLoader, cannot inject libraries. "
                + "The plugin may not function correctly without the following libraries shaded in.");
            return false;
        }

        try {
            Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);

            for (Path jar : this.downloadedJars) {
                URL jarUrl = jar.toUri().toURL();
                addUrlMethod.invoke(urlClassLoader, jarUrl);
                this.logger.fine("Injected " + jar.getFileName() + " into plugin classpath");
            }

            this.logger.info("Injected " + this.downloadedJars.size() + " downloaded libraries into classpath");
            return true;
        } catch (Exception e) {
            this.logger.warning("Failed to inject libraries into classpath: " + e.getMessage());
            return false;
        }
    }

    public List<Path> getDownloadedJars() {
        return List.copyOf(this.downloadedJars);
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        }
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}
