package com.fsn.template.application.configuration

import com.fsn.template.application.account.adapter.request.CreateAccountRequest
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureValidation() {
    install(RequestValidation) {
        // Account request validations
        validate<CreateAccountRequest> {
            request -> request.validate()
        }
    }
}