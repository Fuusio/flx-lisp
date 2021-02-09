package org.fuusio.flx.core.type

enum class ArgModifier {
    OPTIONAL,
    REPEATABLE,
    REQUIRED,
    VARARG;

    fun isOptional() = this == OPTIONAL

    fun isRepeatable() = this == REPEATABLE

    fun isRequired() = this == REQUIRED

    fun isVararg() = this == VARARG
}