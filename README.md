Dcombat is a Paper/Folia combat plugin for Minecraft servers. It includes combat tagging, combat logging behavior, spawn/region handling, visual combat borders, configurable restrictions, and integrations with common server plugins.

Features
Combat tag timer and fight state API
Combat logout handling
Configurable item/experience drop behavior on combat death
Ender pearl, trident, elytra, flying, inventory, command, and block-place restrictions
Region-aware knockback and border feedback
Optional visual effects, lightning, fireworks, and particles
PlaceholderAPI, WorldGuard, Lands, Vault, and LifestealCore integration points
Folia support
Requirements
Java 21
Maven 3.9+
Paper-compatible Minecraft server using API 1.19 or newer
PacketEvents installed on the server
Optional integrations:

PlaceholderAPI
WorldGuard
Lands
Vault
LifestealCore (https://www.spigotmc.org/resources/1-8-8-26-1-2-lifestealcore-the-premium-lifesteal-experience-now-with-custom-textures.101284/)
Build
mvn clean package
The compiled plugin jar is produced in target/.

Install
Build the plugin with Maven.
Copy the generated jar from target/ into your server's plugins/ folder.
Install PacketEvents and any optional integration plugins you want to use.
Restart the server.
Edit the generated plugins/Dcombat/config.yml.
Use /dcombat reload after configuration changes.
The plugin also writes a wiki.txt file into its data folder with configuration documentation.

Repository Setup
This repository is intended to track source code, resources, and project metadata only.

Tracked:

src/main/java/
src/main/resources/
pom.xml
GitHub workflow files
Documentation
Ignored:

Maven build output in target/
IDE settings
Packaged jars and temporary files
GitHub Workflow
Pushes and pull requests run a Maven build using Java 21. See .github/workflows/build.yml.

Release Checklist
Confirm the plugin version in pom.xml and src/main/resources/plugin.yml.
Run mvn clean package.
Test the jar on a Paper server with PacketEvents installed.
Create a GitHub release and attach the built jar.
Add release notes describing changes, fixes, and compatibility.
License
Copyright (c) 2026 Devamy. All rights reserved.

This project is proprietary source code. No permission is granted to copy, modify, distribute, sell, sublicense, or claim this code as someone else's work without prior written permission from the copyright holder.