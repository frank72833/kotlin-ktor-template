/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("buildlogic.kotlin-library-conventions")
}

tasks.jar {
    archiveFileName.set("example-domain-domain.jar")
}

dependencies {
    implementation(project(":example-domain:core"))

    // ## Functional Programming ##
    implementation(libs.arrow.core)

    // ## Testing ##
    testImplementation(libs.kotlin.test.junit)
}
