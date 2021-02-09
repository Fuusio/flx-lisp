/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package org.fuusio.flx.core.type

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.fuusio.flx.core.*
import org.fuusio.flx.core.function.FunctionFormSymbol
import org.fuusio.flx.core.function.FunctionObject
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.macro.MacroSymbol
import org.fuusio.flx.core.rx.Observable
import org.fuusio.flx.core.rx.Subscription
import java.util.*
import kotlin.reflect.KClass

@Suppress("unused")
enum class CoreType(private val argType: KClass<*>) : FlxType {

    ANY(Any::class),
    ARRAY(Array::class),
    BOOLEAN(Boolean::class),
    BYTE(Byte::class),
    CALLBACK(Callback::class),
    CHAR(Char::class),
    CLASS(Class::class),
    CODE(Code::class),
    COLOR(Color::class),
    DATE(Date::class),
    DOUBLE(Double::class),
    FLOAT(Float::class),
    FUNCTION(FunctionObject::class),
    FUNCTION_SYMBOL(FunctionSymbol::class),
    FUNCTION_FORM_SYMBOL(FunctionFormSymbol::class),
    INFO(Info::class),
    INT(Int::class),
    INDEXABLE(Indexable::class),
    JSON_ARRAY(JsonArray::class),
    JSON_ELEMENT(JsonElement::class),
    JSON_OBJECT(JsonObject::class),
    JSON_PRIMITIVE(JsonPrimitive::class),
    LIST(List::class),
    LONG(Long::class),
    MACRO_SYMBOL(MacroSymbol::class),
    MAP(Map::class),
    NULL(Null::class),
    NUMBER(Number::class),
    OBSERVABLE(Observable::class),
    OPTIONS(Options::class),
    PAIR(Pair::class),
    PARSED_FORM(ParsedForm::class),
    REGEX(Regex::class),
    SEQUENCE(Sequence::class),
    SET(Set::class),
    SHORT(Short::class),
    SIZEABLE(Sizable::class),
    STRING(String::class),
    SUBSCRIPTION(Subscription::class),
    SYMBOL(Symbol::class),
    SYSTEM_SYMBOL(SystemSymbol::class),

    VARIABLE(Variable::class);

    override fun getTypeClass() = argType

    override fun isInstance(any: Any): Boolean =
        argType.isInstance(any)

    override fun getTypeName(): String =
        argType.simpleName!!

    companion object {

        fun resolveType(typeName: String): FlxType? {
            values().forEach { value ->
                if (value.getTypeName() == typeName) return value
            }
            return null
        }
    }
}