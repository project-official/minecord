plugins {
    kotlin("jvm") version "1.5.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    `maven-publish`
}

group = "xyz.netherald"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")

    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
    }

    //maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.kyori:adventure-api:4.7.0")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    implementation("net.dv8tion:JDA:4.2.1_253") {
        exclude("club.minnced", "opus-java")
    }

    //compileOnly("com.comphenix.protocol:ProtocolLib:4.6.0")
}
/*
spigot {
    authors = listOf("명이","나무트리","프로젝트")
    apiVersion = "1.16"
        //depends = listOf("ProtocolLib")
    commands {
        //create("hello")
    }
}
 */

val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {

    javadoc {
        options.encoding = "UTF-8"
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }
    }

    create<Jar>("sourceJar") {
        archiveClassifier.set("source")
        from(sourceSets["main"].allSource)
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveVersion.set("")
        archiveClassifier.set("")
    }

    // From monun/tap-sample-plugin
    create<Copy>("copyToServer") {
        from(shadowJar)
        val plugins = File(rootDir, ".server/plugins")
        if (File(shade.artifacts.files.asPath).exists()) {
            into(File(plugins, "update"))
        } else {
            into(plugins)
        }
    }
}