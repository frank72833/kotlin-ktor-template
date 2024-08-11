package com.fsn.template.core.errors

sealed interface ApplicationError {
  val cause: Throwable?
  val message: String

  interface NonRetryableError : ApplicationError

  interface RetryableError : ApplicationError

  interface NotFoundError : RetryableError

  interface OptimisticLockError: RetryableError
}
