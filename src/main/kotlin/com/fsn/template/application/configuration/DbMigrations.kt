package com.fsn.template.application.configuration

import io.ktor.server.application.Application
import org.flywaydb.core.Flyway

fun Application.configureFlyway() {
    val dbUrl = environment.config.property("storage.dbUrl").getString()
    val username = environment.config.property("storage.username").getString()
    val password = environment.config.property("storage.password").getString()

    val flyway = Flyway.configure().dataSource(
        dbUrl,
        username,
        password
    ).load()

    flyway.migrate()
}