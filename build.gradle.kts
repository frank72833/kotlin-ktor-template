import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.builtins.StandardNames.FqNames.target

val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val flyway_version: String by project
val hikari_version: String by project
val mysql_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.diffplug.spotless") version "7.0.0.BETA1"
}

group = "com.fsn.template"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

spotless {
    kotlin {
        ktfmt("0.51").googleStyle()
    }
}

// Marge flyway services due to clashing in fatJar
tasks.withType<ShadowJar> {
    mergeServiceFiles {
        setPath("META-INF/services/org.flywaydb.core.extensibility.Plugin")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // ## API ##
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    // ## DB ##
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    // Required to work with datetime in DB
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    implementation("mysql:mysql-connector-java:$mysql_version")
    // Flyway
    implementation("org.flywaydb:flyway-core:$flyway_version")
    implementation("org.flywaydb:flyway-mysql:$flyway_version")
    // ## Logging ##
    implementation("ch.qos.logback:logback-classic:$logback_version")
    // ## Testing ##
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
