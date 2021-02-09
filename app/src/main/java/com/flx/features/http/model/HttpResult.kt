/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package com.flx.features.http.model

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.fuusio.flx.core.util.Literals
import kotlin.Exception

data class HttpResult(
    val request: Request,
    val response: Response,
    val result: Result<Any, Exception>
) {

    fun getException(): Exception =
        when (result) {
            is Result.Failure -> result.getException()
            else -> NoException
        }

    fun getStatusCode() = response.statusCode

    fun getValue(): String =
        when (result) {
            is Result.Success -> result.value.toString()
            else -> Literals.EMPTY_STRING
        }

    fun getValueAsJson(): JsonObject =
        JsonParser.parseString(getValue()).asJsonObject

    fun isFailure() = result is Result.Failure

    fun isSuccess() = result is Result.Success
}