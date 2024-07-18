package com.fsn.template.core

import java.util.Currency

private val currencyCodeString = Currency.getAvailableCurrencies().map { it.currencyCode }

fun currencyExists(currencyCode: String): Boolean = currencyCodeString.contains(currencyCode)
