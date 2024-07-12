package com.fsn.template.application.configuration

import com.fsn.template.application.account.adapter.request.CreateAccountApiRequest
import com.fsn.template.application.account.adapter.request.UpdateAccountApiRequest
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureValidation() {
  install(RequestValidation) {
    // Account request validations
    validate<CreateAccountApiRequest> { request -> request.validate() }
    validate<UpdateAccountApiRequest> { request -> request.validate() }
  }
}
