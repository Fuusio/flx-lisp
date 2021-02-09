package com.flx.features.http.model

object NoException : Exception() {

    override val message: String?
        get() = "No exception"
}