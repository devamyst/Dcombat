package com.devamy.dcombat.library;

import java.util.List;

public final class KnownLibraries {

    private static final String MAVEN_CENTRAL = "https://repo1.maven.org/maven2";
    private static final String CODEMC = "https://repo.codemc.io/repository/maven-releases";

    public static final LibraryDefinition XSERIES = new LibraryDefinition(
        "com.github.cryptomorin",
        "XSeries",
        "13.8.0",
        CODEMC
    );

    public static final LibraryDefinition CAFFEINE = new LibraryDefinition(
        "com.github.ben-manes.caffeine",
        "caffeine",
        "3.2.4",
        MAVEN_CENTRAL
    );

    public static List<LibraryDefinition> all() {
        return List.of(XSERIES, CAFFEINE);
    }

    private KnownLibraries() {
    }
}
