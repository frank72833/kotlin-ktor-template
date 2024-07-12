import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val flyway_version: String by project
val hikari_version: String by project
val mysql_version: String by project
val jooq_kotlin_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.diffplug.spotless") version "7.0.0.BETA1"
    id("org.jooq.jooq-codegen-gradle") version "3.19.10"
}

group = "com.fsn.template"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

// Merge flyway services due to clashing in fatJar
tasks.withType<ShadowJar> {
    mergeServiceFiles {
        setPath("META-INF/services/org.flywaydb.core.extensibility.Plugin")
    }
}

tasks.named("compileKotlin") {
    dependsOn(tasks.named("jooqCodegen"))
}

jooq {
    configuration {
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            generate {
                isKotlinNotNullPojoAttributes = true
                isKotlinNotNullRecordAttributes = true
                isKotlinNotNullInterfaceAttributes = true
            }
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"

                properties {

                    // Specify the location of your SQL script.
                    // You may use ant-style file matching, e.g. /path/**/to/*.sql
                    //
                    // Where:
                    // - ** matches any directory subtree
                    // - * matches any number of characters in a directory / file name
                    // - ? matches a single character in a directory / file name
                    property {
                        key = "scripts"
                        value = "src/main/resources/db/migration/*.sql"
                    }

                    // The sort order of the scripts within a directory, where:
                    //
                    // - semantic: sorts versions, e.g. v-3.10.0 is after v-3.9.0 (default)
                    // - alphanumeric: sorts strings, e.g. v-3.10.0 is before v-3.9.0
                    // - flyway: sorts files the same way as flyway does
                    // - none: doesn't sort directory contents after fetching them from the directory
                    property {
                        key = "sort"
                        value = "flyway"
                    }

                    // The default schema for unqualified objects:
                    //
                    // - public: all unqualified objects are located in the PUBLIC (upper case) schema
                    // - none: all unqualified objects are located in the default schema (default)
                    //
                    // This configuration can be overridden with the schema mapping feature
                    property {
                        key = "unqualifiedSchema"
                        value = "none"
                    }

                    // The default name case for unquoted objects:
                    //
                    // - as_is: unquoted object names are kept unquoted
                    // - upper: unquoted object names are turned into upper case (most databases)
                    // - lower: unquoted object names are turned into lower case (e.g. PostgreSQL)
                    property {
                        key = "defaultNameCase"
                        value = "as_is"
                    }
                }
            }
        }
    }
}

spotless {
    kotlin {
        ktfmt("0.51").googleStyle()
        targetExclude("**/build/**/*")
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
    implementation("org.jooq:jooq-kotlin:$jooq_kotlin_version")
    implementation("org.jooq:jooq-kotlin-coroutines:$jooq_kotlin_version")
    // JOOQ code generator
    jooqCodegen("org.jooq:jooq-meta-extensions:$jooq_kotlin_version")
    // Required to work with datetime in DB
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
