plugins {
    java
    id("net.fabricmc.fabric-loom") version "1.17.1"
}

version = project.property("mod_version").toString()
group = "com.example.cursorkeep"

base {
    archivesName.set("cursorkeep")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    withSourcesJar()
}

repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    mavenCentral()
}

loom {
    // Single source set is enough for this client-only mod.
    // NOTE (26.1.2): Minecraft 26.1 is unobfuscated, so no `mappings(...)` line
    // is needed anymore (matches official fabric-example-mod for 26.1).
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version")}")
    // Fabric API. Technically optional, kept as in the official 26.1 template.
    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
}

tasks.withType<JavaCompile> {
    options.release.set(25)
}

tasks.processResources {
    val modVersion: String = project.version.toString()
    val mcVersion: String = project.property("minecraft_version").toString()
    val loaderVersion: String = project.property("fabric_loader_version").toString()
    inputs.property("version", modVersion)
    inputs.property("minecraft_version", mcVersion)
    inputs.property("loader_version", loaderVersion)
    filesMatching("fabric.mod.json") {
        expand(
            "version" to modVersion,
            "minecraft_version" to mcVersion,
            "loader_version" to loaderVersion
        )
    }
}
