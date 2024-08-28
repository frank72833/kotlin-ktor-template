import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("buildlogic.kotlin-application-conventions")
    id("io.ktor.plugin") version "2.3.12"
    id("com.diffplug.spotless") version "7.0.0.BETA1"
    id("com.gradleup.shadow") version "8.3.0"
}

// Merge flyway services due to clashing in fatJar
tasks.withType<ShadowJar> {
    mergeServiceFiles {
        setPath("META-INF/services/org.flywaydb.core.extensibility.Plugin")
    }
}

spotless {
    kotlin {
        ktfmt("0.51").googleStyle()
        targetExclude("**/build/**/*")
    }
}

dependencies {
    implementation(project(":example-domain:application"))
}