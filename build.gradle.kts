plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.1"

    `maven-publish`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

group = "org.propercrew"
version = "1.1.2"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion:JDA:5.0.0-alpha.2") {
        exclude(module="opus-java")
    }
    compileOnly("org.apache.logging.log4j:log4j-core:2.17.0")
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_16.toString()
    }

    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveVersion.set("")
        archiveClassifier.set("")
    }
}
