plugins {
    kotlin("jvm")
}

val kotlinVersion: String by project
val jdaVersion: String by project
val minecraftVersion: String by project
val webhooksVersion: String by project
val adventureVersion: String by project
val mcdiscordreserializerVersion: String by project

group = "dev.cube1"
version = "4.0.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("club.minnced:discord-webhooks:$webhooksVersion")
    implementation("net.kyori:adventure-text-minimessage:$adventureVersion")
    implementation("dev.vankka:mcdiscordreserializer:$mcdiscordreserializerVersion")
    compileOnly("io.papermc.paper:paper-api:${minecraftVersion}-R0.1-SNAPSHOT")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    create<Jar>("paperJar") {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")

        from(sourceSets["main"].output)
    }
}