package com.devamy.dcombat.library;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public record LibraryDefinition(String groupId, String artifactId, String version, String repoUrl) {

    public String jarFileName() {
        return this.artifactId + "-" + this.version + ".jar";
    }

    public String mavenUrl() {
        String groupPath = this.groupId.replace('.', '/');
        return this.repoUrl
            + "/" + groupPath
            + "/" + this.artifactId
            + "/" + this.version
            + "/" + this.artifactId + "-" + this.version + ".jar";
    }
}
