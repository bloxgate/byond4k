import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.3.60"
    `maven-publish`
    id("org.jetbrains.dokka") version "0.10.0"
}

group = "me.gmaddra.byond4k"
version = "1.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

publishing {
    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/bloxgate/byond4k")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GPR_TOKEN")
            }
        }
    }

    publications {
        register("gpr", MavenPublication::class) {
            from(components["java"])
            artifact("documentationJar")
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }
}

val documentationJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Documentation files"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}