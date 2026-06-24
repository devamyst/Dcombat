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

public class LibraryLoader {

    private static final int CONNECT_TIMEOUT = 10_000;
    private static final int READ_TIMEOUT = 30_000;

    private final Path libsDirectory;
    private final Logger logger;
    private final List<LibraryDefinition> libraries;
    private final List<URL> libraryUrls = new ArrayList<>();
    private URLClassLoader classLoader;

    public LibraryLoader(Path dataFolder, Logger logger, List<LibraryDefinition> libraries) {
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
            this.addLibrary(jarPath);
            return;
        }

        this.logger.info("Downloading library: " + lib.artifactId() + " v" + lib.version() + "...");

        try {
            URL url = new URI(lib.mavenUrl()).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("User-Agent", "Dcombat-LibraryLoader");

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

            this.addLibrary(jarPath);
            this.logger.info("Downloaded " + lib.artifactId() + " v" + lib.version() + " (" + this.formatSize(actualSize) + ")");
        } catch (IOException | URISyntaxException e) {
            this.logger.warning("Could not download " + lib.artifactId() + ": " + e.getMessage());
        }
    }

    private void addLibrary(Path jarPath) {
        try {
            this.libraryUrls.add(jarPath.toUri().toURL());
        } catch (IOException e) {
            this.logger.warning("Could not resolve library path: " + e.getMessage());
        }
    }

    public URLClassLoader createClassLoader(ClassLoader parent) {
        if (this.libraryUrls.isEmpty()) {
            this.classLoader = new URLClassLoader(new URL[0], parent);
            return this.classLoader;
        }

        this.classLoader = new URLClassLoader(
            this.libraryUrls.toArray(new URL[0]),
            parent
        );

        this.logger.info("Created library classloader with " + this.libraryUrls.size() + " jars");
        return this.classLoader;
    }

    public boolean injectInto(URLClassLoader target) {
        if (this.libraryUrls.isEmpty()) {
            return true;
        }

        try {
            Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);

            for (URL url : this.libraryUrls) {
                addUrlMethod.invoke(target, url);
                this.logger.fine("Injected " + url.getFile() + " into " + target.getClass().getSimpleName());
            }

            this.logger.info("Injected " + this.libraryUrls.size() + " libraries into plugin classpath");
            return true;
        } catch (Exception e) {
            this.logger.warning("Failed to inject libraries into classpath: " + e.getMessage());
            this.logger.warning("Plugin may not function correctly - libraries will be loaded via the child classloader");
            return false;
        }
    }

    public URLClassLoader getClassLoader() {
        return this.classLoader;
    }

    public List<URL> getLibraryUrls() {
        return List.copyOf(this.libraryUrls);
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
