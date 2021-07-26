plugins {
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    `maven-publish`
}

group = "xyz.netherald"
version = "2.3.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")

    //maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion:JDA:4.3.0_277")
    implementation("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    // compileOnly("com.comphenix.protocol:ProtocolLib:4.6.0")
}
/*
spigot {
    authors = listOf("명이","나무트리","프로젝트")
    apiVersion = "1.17"
    //depends = listOf("ProtocolLib")

    commands {
        //create("hello")
    }
}
 */

tasks {
    javadoc {
        options.encoding = "UTF-8"
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
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
/*
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

 */
}
