/*
 * Copyright (C) 2016 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("UNUSED_PARAMETER")

package org.fuusio.flx.core.function

import com.google.gson.*
import org.fuusio.flx.Flx
import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.*
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Map
import org.fuusio.flx.core.type.Set
import org.fuusio.flx.core.parser.*
import org.fuusio.flx.core.rx.Observable
import org.fuusio.flx.core.rx.PeriodicTimerObservable
import org.fuusio.flx.core.rx.Subscription
import org.fuusio.flx.core.rx.TimerObservable
import org.fuusio.flx.core.type.*
import org.fuusio.flx.core.type.impl.ImmutableArray
import org.fuusio.flx.core.type.impl.ImmutableList
import org.fuusio.flx.core.type.impl.ImmutableMap
import org.fuusio.flx.core.type.impl.ImmutableSet
import org.fuusio.flx.core.vm.Ctx
import java.lang.reflect.Field
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.random.Random
import kotlin.reflect.KClass

fun fnLogD(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Null {
    Flx.logger.debug(args[0].toString())
    return Null
}

fun fnLogI(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Null {
    Flx.logger.info(args[0].toString())
    return Null
}

fun fnLogW(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Null {
    Flx.logger.warning(args[0].toString())
    return Null
}

fun fnLogE(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Null {
    Flx.logger.error(args[0].toString())
    return Null
}

fun fnNoop(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int) = Null

fun fnAbs(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number =
    when (val number = args[0] as Number) {
        is Float -> number.toDouble().absoluteValue
        is Double -> number.absoluteValue
        else -> number.toLong().absoluteValue
    }

fun fnAcos(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.acos((args[0] as Number).toDouble())

fun fnAddition(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    var value = 0.0
    var isLong = true

    for (i in args.indices) {
        when (val arg = args[i]) {
            is Float -> {
                isLong = false
                value += arg.toDouble()
            }
            is Double -> {
                isLong = false
                value += arg
            }
            is Number -> value += arg.toDouble()
            else -> throw IllegalStateException()
        }
    }
    return if (isLong) value.toLong() else value
}

fun fnAnd(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    for (arg in args) if (arg.isFalse()) return false
    return true
}

fun fnAsin(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.asin((args[0] as Number).toDouble())

fun fnAtan(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.atan((args[0] as Number).toDouble())

fun fnIsBlank(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean =
    when(val value = args[0]) {
        is String -> value.isBlank()
        is Null -> true
        else -> throw IllegalStateException()
    }

fun fnButlast(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val count = (args[0] as Number).toInt()
    return when (val string = args[1]) {
        is String -> {
            val length = string.length
            return if (count > length) "" else string.substring(0, length - count)
        }
        is Null -> ""
        else -> throw IllegalStateException()
    }
}

fun fnBitAnd(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    var result = args[0] as Long and args[1] as Long
    for (i in 2 until args.size) result = result and args[i] as Long
    return result
}

fun fnBitNot(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long = (args[0] as Long).inv()

fun fnBitOr(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    var result = args[0] as Long or args[1] as Long
    for (i in 2 until args.size) result = result or args[i] as Long
    return result
}

fun fnShl(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long = (args[0] as Long).shl((args[1] as Number).toInt())

fun fnShr(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long = (args[0] as Long).shr((args[1] as Number).toInt())

fun fnBitXor(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    var result = args[0] as Long xor args[1] as Long
    for (i in 2 until args.size) result = result xor args[i] as Long
    return result
}

fun fnCeil(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.ceil((args[0] as Number).toDouble())

@ExperimentalStdlibApi
fun fnCapitalize(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String = (args[0] as String).capitalize(
    Locale.ENGLISH
)

fun fnChop(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val string = args[0] as String
    return if (string.isNotEmpty()) string.substring(0, string.length - 1) else string
}

fun fnColor(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Color {
    val alpha = if (args.size == 4) normalizeColorComponent((args[3] as Number).toInt()) else 255
    val red = normalizeColorComponent((args[0] as Number).toInt())
    val green = normalizeColorComponent((args[1] as Number).toInt())
    val blue = normalizeColorComponent((args[2] as Number).toInt())
    return Color(android.graphics.Color.argb(alpha, red, green, blue))
}

private fun normalizeColorComponent(value: Int): Int =
    when {
        value < 0 -> 0
        value > 255 -> 255
        else -> value
    }

fun fnCompare(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val string1 = args[0] as String
    val string2 = args[1] as String
    return string1.compareTo(string2).toLong()
}

fun fnConj(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    when (val collection = args[0]) {
        is Array -> collection.conj(args[1])
        is List -> collection.conj(args[1])
        is Set -> collection.conj(args[1])
        is Null -> collection.conj(args[1])
        else -> throw IllegalStateException()
    }

fun fnCons(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): List =
    when (val sequence = args[1]) {
        is Sequence -> sequence.cons(args[0])
        is String -> sequence.cons(args[0])
        is Set -> sequence.cons(args[0])
        is Null -> sequence.cons(args[0])
        else -> throw IllegalStateException()
    }

fun fnCos(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.cos((args[0] as Number).toDouble())

fun fnDate(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Date = Date()


fun fnGetYear(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.time = args[0] as Date
    return calendar.get(Calendar.YEAR).toLong()
}

fun fnGetDay(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.time = args[0] as Date
    return calendar.get(Calendar.DAY_OF_MONTH).toLong()
}

fun fnGetMonth(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.time = args[0] as Date
    return (calendar.get(Calendar.MONTH) + 1).toLong()
}

fun fnGetHour(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.time = args[0] as Date
    return calendar.get(Calendar.HOUR_OF_DAY).toLong()
}

fun fnGetMinute(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.time = args[0] as Date
    return calendar.get(Calendar.MINUTE).toLong()
}

fun fnGetSecond(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.time = args[0] as Date
    return calendar.get(Calendar.SECOND).toLong()
}

fun fnGetMillisecond(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.time = args[0] as Date
    return calendar.get(Calendar.MILLISECOND).toLong()
}

//fun fnDateMilliseconds(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
//    val calendar = Calendar.getInstance()
//    calendar.time = args[0] as Date
//    return calendar.get(Calendar.MILLISECOND).toLong()
//}

fun fnDec(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
        return when (val number = args[0]) {
            is Float -> number.toDouble() - 1
            is Double -> number - 1
            is Number -> number.toLong() - 1
            else -> throw IllegalStateException()
        }
    }


fun fnDivision(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    var value = 0.0
    val argsCount = args.size

    if (argsCount > 0) {
        when (val arg = args[0]) {
            is Number -> value = arg.toDouble()
            else -> throw IllegalStateException()
        }

        if (value >= -EPSILON && value <= EPSILON)
            throw ZeroDivisionException(ctx)

        for (i in 1 until argsCount) {
            when (val number = args[i]) {
                is Number -> value /= number.toDouble()
                else -> throw IllegalStateException()
            }
        }
    }
    return value
}

fun fnDrop(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val count = (args[0] as Number).toInt()
    return when (val string = args[1]) {
        is String -> string.drop(count)
        is Null -> ""
        else -> throw IllegalStateException()
    }
}

fun fnDropLast(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val count = (args[0] as Number).toInt()
    return when (val string = args[1]) {
        is String -> string.dropLast(count)
        is Null -> ""
        else -> throw IllegalStateException()
    }
}

fun fnEqual(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    val first = args[0]
    for (i in 1 until args.size) if (first != args[i]) return false
    return true
}

fun fnEndsWith(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean
        = (args[0] as String).endsWith(args[1] as String)

fun fnEpochTime(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long = System.currentTimeMillis()

fun fnEval(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int) = args[0].eval(ctx)

fun fnEvalString(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int) = fnParse(
    ctx,
    args,
    signatureIndex
).eval(ctx)

fun fnExp(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.exp((args[0] as Number).toDouble())

fun fnFilter(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): List {
    val predicate = args[0] as FunctionObject
    val list = when (val collection = args[1]) {
        is Array -> collection.toList()
        is List -> collection
        is Map -> collection.toList()
        is Set -> collection.toList()
        else -> throw IllegalStateException()
    }
    return list.filter(ctx, predicate)
}

fun fnFirst(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    when (val arg = args[0]) {
        is String -> arg.first()
        is Sequence -> arg.first()
        else -> throw IllegalStateException()
    }

fun fnFloor(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.floor((args[0] as Number).toDouble())

fun fnFormat(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int) : String {
    val argsCount = args.size
    var string = when (val form = args[0]) {
        is String -> form
        else -> form.toLiteral()
    }

    if (args.size > 1) {
        val formatArgs = (1 until argsCount).map { args[it] }.toTypedArray()
        try {
            string = string.format(*formatArgs)
        } catch (e: Exception) {
            throw StringFormatException(ctx, e.message!!)
        }
    }
    return string
}

fun fnGet(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    when (signatureIndex) {
        0 -> fnGet0(args)
        1 -> fnGet0(args)
        2 -> fnGet1(args)
        3 -> fnGet1(args)
        else -> throw IllegalStateException()
    }

fun fnJsonGet(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    fnGet0(args)

private fun fnGet0(args: kotlin.Array<Any>): Any {
    var jsonObject = args[0] as JsonObject
    val field = when(val key = args[1]) {
        is String -> key
        else -> key.toLiteral()
    }
    if (field.contains('/')) {
        val fields = field.split("/")
        fields.forEach { key ->
            when (val value = jsonObject.get(key)) {
                is JsonObject -> jsonObject = value
                else -> return value
            }
        }
        return jsonObject
    } else {
        return jsonObject.get(field) ?: if (args.size == 3) args[2] else Null
    }
}

private fun fnGet1(args: kotlin.Array<Any>): Any {
    val map = args[0] as Map
    return map.get(args[1], if (args.size == 3) args[2] else Null)
}

fun fnGreaterThan(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    var value = (args[0] as Number).toDouble()

    for (i in 1 until args.size) {
        val nextValue = (args[i] as Number).toDouble()
        if (value <= nextValue) return false
        value = nextValue
    }
    return true
}

fun fnGreaterThanOrEqual(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    var value = (args[0] as Number).toDouble()

    for (i in 1 until args.size) {
        val nextValue = (args[i] as Number).toDouble()
        if (value < nextValue) return false
        value = nextValue
    }
    return true
}

fun fnIdentical(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean =
    args[0] === args[1]

fun fnInc(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    return when (val number = args[0]) {
        is Float -> number.toDouble() + 1
        is Double -> number + 1
        is Number -> number.toLong() + 1
        else -> throw IllegalStateException()
    }
}

fun fnIndexOf(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Int =
    (args[0] as String).indexOf((args[1] as String), args[2] as Int)

fun fnIsArray(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isArray()

fun fnIsBoolean(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isBoolean()

fun fnIsByte(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isByte()

fun fnIsDoubleQ(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isDouble()

fun fnIsEmpty(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean =
    when (val arg = args[0]) {
        is String -> arg.isEmpty()
        is Sizable -> arg.isEmpty()
        else -> throw IllegalStateException()
    }

fun fnIsNotEmpty(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean =
    when (val arg = args[0]) {
        is String -> arg.isNotEmpty()
        is Sizable -> arg.isNotEmpty()
        else -> throw IllegalStateException()
    }

fun fnIsEven(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = (args[0] as Number).toLong() % 2 == 0L

fun fnIsFalse(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isFalse()

fun fnIsJsonArray(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0] is JsonArray

fun fnIsJsonElement(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0] is JsonElement

fun fnIsJsonObject(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0] is JsonObject

fun fnIsJsonPrimitive(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0] is JsonPrimitive

fun fnIsMap(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isMap()

fun fnIsList(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isList()

fun fnIsLong(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isLong()

fun fnIsNegative(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = (args[0] as Number).toDouble() < 0

fun fnIsNull(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isNil()

fun fnIsOdd(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = (args[0] as Number).toLong() % 2 != 0L

fun fnIsPositive(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = (args[0] as Number).toDouble() > 0

fun fnIsSet(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isSet()

fun fnIsString(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isString()

fun fnIsSymbol(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isSymbol()

fun fnIsTrue(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].isTrue()

private const val EPSILON = 0.000000000001

fun fnIsZero(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean =
    when (val number = (args[0] as Number)) {
        is Double -> number >= -EPSILON && number <= EPSILON
        is Float -> number >= -EPSILON && number <= EPSILON
        else -> number == 0
    }

fun fnLast(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    when (val arg = args[0]) {
        is String -> arg.last()
        is Sequence -> arg.last()
        else -> throw IllegalStateException()
    }

fun fnGetClass(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Class<*> =
    args[0]::class.java

fun fnIsInstance(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean =
    when (val instanceClass  = args[1]) {
        is KClass<*> -> instanceClass.java.isInstance(args[0])
        is Class<*> -> instanceClass.isInstance(args[0])
        else -> instanceClass::class.java.isInstance(args[0])
    }

fun fnClassForName(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Class<*> {
    val className = args[0].toString()
    return Class.forName(className)
}

fun fnClassNew(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val javaClass = args[0] as Class<*>
    val paramsCount = args.size - 1
    val params = arrayOfNulls<Any>(paramsCount)

    repeat(paramsCount) { i -> params[i] = args[i + 1] }

    try {
        return if (paramsCount >=0) {
            val paramTypes = arrayOfNulls<Class<*>>(paramsCount)
            repeat(paramsCount) { i -> paramTypes[i] = getClass(args[i + 1]) }
            val constructor = javaClass.getConstructor(*paramTypes)
            constructor.newInstance(*params)
        } else {
            javaClass.newInstance()
        }
    } catch (e: NoSuchMethodException) {
        throw e
    }
}

fun fnInvoke(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val invokee = if (args[0] is Class<*>) null else args[0]
    val invokeeClass = invokee?.javaClass ?: args[0] as Class<*>
    val methodName = args[1] as String
    val paramsCount = args.size - 2
    val params = arrayOfNulls<Any>(paramsCount)

    repeat(paramsCount) { i -> params[i] = args[i + 2] }

    try {
        val method = if (paramsCount >= 0) {
            val paramTypes = arrayOfNulls<Class<*>>(paramsCount)
            repeat(paramsCount) { i -> paramTypes[i] = getClass(args[i + 2]) }
            invokeeClass.getMethod(methodName, *paramTypes)
        } else {
            invokeeClass.getMethod(methodName, null)
        }
        return method.invoke(invokee, *params) ?: Null
    } catch (e: NoSuchMethodException) {
        throw e
    }
}

fun fnGetProperty(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val invokee = if (args[0] is Class<*>) null else args[0]
    val invokeeClass = invokee?.javaClass ?: args[0] as Class<*>
    val propertyName = args[1] as String

    try {
        val field: Field = invokeeClass.getDeclaredField(propertyName)
        field.isAccessible = true
        return field.get(invokee) ?: Null
    } catch (e: Exception) {
        throw e
    }
}

fun fnSetProperty(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Null {
    val invokee = if (args[0] is Class<*>) null else args[0]
    val invokeeClass = invokee?.javaClass ?: args[0] as Class<*>
    val propertyName = args[1] as String
    val value = args[2]

    try {
        val field: Field = invokeeClass.getDeclaredField(propertyName)
        field.isAccessible = true
        field.set(invokee, value)
        return Null
    } catch (e: Exception) {
        throw e
    }
}

private fun getClass(arg: Any): Class<*> =
    when (arg) {
        is Boolean -> java.lang.Boolean.TYPE
        is Byte -> java.lang.Byte.TYPE
        is Char -> Character.TYPE
        is Double -> java.lang.Double.TYPE
        is Float -> java.lang.Float.TYPE
        is Int -> Integer.TYPE
        is Long -> java.lang.Long.TYPE
        is Short -> java.lang.Short.TYPE
        else -> arg.javaClass
    }

fun fnLesserThan(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    var value = (args[0] as Number).toDouble()

    for (i in 1 until args.size) {
        val nextValue = (args[i] as Number).toDouble()
        if (value >= nextValue) return false
        value = nextValue
    }
    return true
}

fun fnLesserThanOrEqual(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    var value = (args[0] as Number).toDouble()

    for (i in 1 until args.size) {
        val nextValue = (args[i] as Number).toDouble()
        if (value > nextValue) return false
        value = nextValue
    }
    return true
}

fun fnLog(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.log((args[0] as Number).toDouble(), (args[1] as Number).toDouble())

fun fnToLowerCase(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String = (args[0] as String).toLowerCase(
    Locale.ENGLISH
)

fun fnMatches(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    val regex = when (val arg = args[1]) {
        is String -> arg.toRegex()
        is Regex -> arg
        else -> throw IllegalStateException()
    }
    return regex.matches(args[0] as String)
}

fun fnMax(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    var max = Double.MIN_VALUE
    var isLong = true

    for (i in args.indices) {
        when (val arg = args[i]) {
            is Float -> {
                if (arg > max) {
                    isLong = false
                    max = arg.toDouble()
                }
            }
            is Double -> {
                if (arg > max) {
                    isLong = false
                    max = arg
                }
            }
            is Number -> {
                val double = arg.toDouble()
                if (double > max) {
                    max = double
                }
            }
            else -> throw IllegalStateException()
        }
    }
    return if (isLong) max.toLong() else max
}

fun fnMin(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    var min = Double.MAX_VALUE
    var isLong = true

    for (i in args.indices) {
        when (val arg = args[i]) {
            is Float -> {
                if (arg < min) {
                    isLong = false
                    min = arg.toDouble()
                }
            }
            is Double -> {
                if (arg < min) {
                    isLong = false
                    min = arg
                }
            }
            is Number -> {
                val double = arg.toDouble()
                if (double < min) {
                    min = double
                }
            }
            else -> throw IllegalStateException()
        }
    }
    return if (isLong) min.toLong() else min
}

fun fnMultiplication(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    var value = 1.0
    var isLong = true

    for (i in args.indices) {
        when (val arg = args[i]) {
            is Float -> {
                isLong = false
                value *= arg.toDouble()
            }
            is Double -> {
                isLong = false
                value *= arg
            }
            is Number -> value *= arg.toDouble()
            else -> throw IllegalStateException()
        }
    }
    return if (isLong) value.toLong() else value
}

fun fnNot(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = !args[0].toBoolean()

fun fnNotEqual(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    val first = args[0]
    for (i in 1 until args.size) if (first != args[i]) return true
    return false
}

fun fnCharAt(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Char {
    val string = args[0] as String
    val index = (args[1] as Number).toInt()
    return string[index]
}

fun fnNth(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val indexable = args[0] as Indexable
    val index = (args[1] as Number).toInt()
    return indexable[index]
}

fun fnOr(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    for (arg in args) if (arg.isTrue()) return true
    return false
}

fun fnParseBoolean(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean =
    args[0].toString().toBoolean()

fun fnParseByte(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    args[0].toString().toByteOrNull() ?: Null

fun fnParseDouble(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    args[0].toString().toDoubleOrNull() ?: Null

fun fnParseFloat(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    args[0].toString().toFloatOrNull() ?: Null

fun fnParseInt(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    args[0].toString().toIntOrNull() ?: Null

fun fnParseLong(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any =
    args[0].toString().toLongOrNull() ?: Null

fun fnPow(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    (args[0] as Number).toDouble().pow((args[1] as Number).toDouble())

fun fnPrint(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int) : Null {
    ctx.getFlxVm().print(fnFormat(ctx, args, signatureIndex))
    return Null
}

fun fnPrintln(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int) : Null {
    ctx.getFlxVm().println(fnFormat(ctx, args, signatureIndex))
    return Null
}

fun fnRand(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double
        = Random(System.currentTimeMillis()).nextDouble()

fun fnParse(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val parserObserver = object : ParserObserver {
        override fun onNext(parsingResult: Any) {}
        override fun onError(error: ParserError) {}
    }
    val results = FormParser(parserObserver).parse(ParsingContext(), args[0] as String)

    return when (val resultsSize = results.size) {
        1 -> results[0]
        else -> results[resultsSize - 1]
    }
}

fun fnParseEval(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val parserObserver = object : ParserObserver {
        override fun onNext(parsingResult: Any) {}
        override fun onError(error: ParserError) {
            throw ParseException(ctx, error, args[0].toString())
        }
    }
    var result: Any = Null
    FormParser(parserObserver).parse(ParsingContext(), args[0].toString()).forEach { form ->
        result = form.eval(ctx)
    }
    return result
}

fun fnCompile(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): ParsedForm =
    ParsedForm(args[0].toString())

fun fnRem(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    val divider = args[1] as Number
    return when (val number = args[0] as Number) {
        is Byte -> rem(number, divider)
        is Double -> rem(number, divider)
        is Float -> rem(number, divider)
        is Int -> rem(number, divider)
        is Long -> rem(number, divider)
        is Short -> rem(number, divider)
        else -> throw IllegalStateException()
    }
}

fun rem(byte: Byte, divider: Number): Number =
    when (divider) {
        is Byte -> byte.rem(divider)
        is Double -> byte.rem(divider)
        is Float -> byte.rem(divider)
        is Int -> byte.rem(divider)
        is Long -> byte.rem(divider)
        is Short -> byte.rem(divider)
        else -> throw IllegalStateException()
    }

fun rem(double: Double, divider: Number): Number =
    when (divider) {
        is Byte -> double.rem(divider)
        is Double -> double.rem(divider)
        is Float -> double.rem(divider)
        is Int -> double.rem(divider)
        is Long -> double.rem(divider)
        is Short -> double.rem(divider)
        else -> throw IllegalStateException()
    }

fun rem(float: Float, divider: Number): Number =
    when (divider) {
        is Byte -> float.rem(divider)
        is Double -> float.rem(divider)
        is Float -> float.rem(divider)
        is Int -> float.rem(divider)
        is Long -> float.rem(divider)
        is Short -> float.rem(divider)
        else -> throw IllegalStateException()
    }

fun rem(int: Int, divider: Number): Number =
    when (divider) {
        is Byte -> int.rem(divider)
        is Double -> int.rem(divider)
        is Float -> int.rem(divider)
        is Int -> int.rem(divider)
        is Long -> int.rem(divider)
        is Short -> int.rem(divider)
        else -> throw IllegalStateException()
    }

fun rem(long: Long, divider: Number): Number =
    when (divider) {
        is Byte -> long.rem(divider)
        is Double -> long.rem(divider)
        is Float -> long.rem(divider)
        is Int -> long.rem(divider)
        is Long -> long.rem(divider)
        is Short -> long.rem(divider)
        else -> throw IllegalStateException()
    }

fun rem(short: Short, divider: Number): Number =
    when (divider) {
        is Byte -> short.rem(divider)
        is Double -> short.rem(divider)
        is Float -> short.rem(divider)
        is Int -> short.rem(divider)
        is Long -> short.rem(divider)
        is Short -> short.rem(divider)
        else -> throw IllegalStateException()
    }

fun fnRepeatString(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val string = args[1]
    val builder = StringBuilder()
    repeat((args[0] as Number).toInt()) { builder.append(string)}
    return builder.toString()
}

fun fnReplaceChar(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val oldChar = args[0] as Char
    val newChar = args[1] as Char
    val string  = args[0] as String
    return string.replace(oldChar, newChar)
}

fun fnReplaceFirstRegex(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val regex = when (val arg = args[0]) {
        is String -> arg.toRegex()
        is Regex -> arg
        else -> throw IllegalStateException()
    }
    return (args[2] as String).replaceFirst(regex, args[1] as String)
}

fun fnReplaceFirstString(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val substring = args[0] as String
    return (args[2] as String).replaceFirst(substring, args[1] as String)
}

fun fnReplaceRegex(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val regex = when (val arg = args[0]) {
        is String -> arg.toRegex()
        is Regex -> arg
        else -> throw IllegalStateException()
    }
    return (args[2] as String).replace(regex, args[1] as String)
}

fun fnReplaceString(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val substring = args[0] as String
    return (args[2] as String).replace(substring, args[1] as String)
}

fun fnRest(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Sequence =
    when (val arg = args[0]) {
        is Sequence -> arg.rest()
        is String -> {
            when (arg.length) {
                0 -> ImmutableArray.emptyArray
                else -> ImmutableArray.create(arg.substring(1))
            }
        }
        else -> throw IllegalStateException()
    }

fun fnReverse(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String = (args[0] as String).reversed()

fun fnRound(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.round((args[0] as Number).toDouble())

fun fnSign(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long {
    val number = (args[0] as Number).toDouble()
    return when {
        number < 0.0 -> -1L
        number > 0.0 -> 1L
        else -> 0L
    }
}

fun fnSqrt(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.sqrt((args[0] as Number).toDouble())

fun fnSin(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.sin((args[0] as Number).toDouble())

fun fnSize(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long =
    when (val arg = args[0]) {
        is String -> arg.length.toLong()
        is Sizable -> arg.size().toLong()
        else -> throw IllegalStateException()
    }

fun fnSplit(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Array {
    val limit = if (args.size == 3) (args[1] as Number).toInt() else Int.MAX_VALUE
    val string = if (args.size == 3) args[2] as String else args[1] as String
    val elements = mutableListOf<Any>()
    val regex = when (val arg = args[0]) {
        is String -> arg.toRegex()
        is Regex -> arg
        else -> throw IllegalStateException()
    }
    val strings = string.split(regex, limit)
    elements.addAll(strings)
    return ImmutableArray.create(elements)
}

fun fnSplitLines(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Array {
    val elements = mutableListOf<Any>()
    val string = (args[0] as String).replace("\r\n", "\n")
    elements.addAll(string.split('\n'))
    return ImmutableArray.create(elements)
}

fun fnStartsWith(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean
        = (args[0] as String).startsWith(args[1] as String)

fun fnStr(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val builder = StringBuilder()
    args.forEach { element -> builder.append(
        when (element) {
            is Null -> ""
            is String -> element
            is Char -> element.toString()
            else -> element.toLiteral()
        }
    )
    }
    return builder.toString()
}

fun fnSubArray(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Array {
    val array = args[0] as Array
    val start = (args[1] as Number).toInt()

    return if (args.size == 2) {
        array.subarray(start, array.size())
    } else {
        val end = (args[2] as Number).toInt()
        array.subarray(start, end)
    }
}

fun fnOnbservableSubscribe(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Subscription {
    val observable = args[0] as Observable
    val observer = args[1] as FunctionObject
    return observable.subscribe(observer)
}

fun fnSubscriptionUnsubscribe(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Observable {
    val subscription = args[0] as Subscription
    val observable = subscription.observable
    observable.unsubscribe(subscription)
    return observable
}

fun fnPeriodicTimerObservable(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): PeriodicTimerObservable {
    val initialDelay = (args[0] as Number).toLong()
    val period = (args[1] as Number).toLong()
    return PeriodicTimerObservable(ctx, initialDelay, period)
}

fun fnTimerObservable(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): TimerObservable {
    val period = (args[0] as Number).toLong()
    return TimerObservable(ctx, period)
}

fun fnSubscritionStart(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Subscription {
    val subscription = args[0] as Subscription
    if (!subscription.observable.start()) {
        val observable = subscription.observable
        throw FailedToStartObservableException(ctx, observable, observable.getErrorDescription())
    }
    return subscription
}

fun fnSubscritionPause(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Subscription {
    val subscription = args[0] as Subscription
    subscription.observable.pause()
    return subscription
}

fun fnSubscritionResume(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Subscription {
    val subscription = args[0] as Subscription
    subscription.observable.resume()
    return subscription
}

fun fnSubscritionStop(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Subscription {
    val subscription = args[0] as Subscription
    subscription.observable.stop()
    return subscription
}


fun fnSubstring(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val string = args[0] as String
    val start = (args[1] as Number).toInt()

    return if (args.size == 2) {
        string.substring(start)
    } else {
        val end = (args[2] as Number).toInt()
        string.substring(start, end)
    }
}

fun fnContains(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = (args[1] as String).contains(
    args[0] as String
)

fun fnPercentage(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    val percentage = (args[0] as Number).toDouble()
    val total = (args[1] as Number).toDouble()
    return percentage / 100.0 * total
}

fun fnSubtraction(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    var value = 0.0
    var isLong = true
    val argsCount = args.size

    if (argsCount > 0) {
        when (val number = args[0]) {
            is Float -> {
                isLong = false
                value = number.toDouble()
            }
            is Double -> {
                isLong = false
                value = number
            }
            is Number -> value = number.toDouble()
            else -> throw IllegalStateException()
        }

        if (argsCount == 1) value = -value

        for (i in 1 until argsCount) {
            when (val number = args[i]) {
                is Float -> {
                    isLong = false
                    value -= number.toDouble()
                }
                is Double -> {
                    isLong = false
                    value -= number
                }
                is Number -> value -= number.toDouble()
                else -> throw IllegalStateException()
            }
        }
    }
    return if (isLong) value.toLong() else value
}

fun fnTail(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val count = (args[0] as Number).toInt()
    return when (val string = args[1]) {
        is String -> {
            val length = string.length
            return if (count > length) "" else string.substring(length - count, length)
        }
        is Null -> ""
        else -> throw IllegalStateException()
    }
}

fun fnTake(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String {
    val count = (args[0] as Number).toInt()
    return when (val string = args[1]) {
        is String -> {
            val length = string.length
            return if (count > length) string else string.substring(0, count)
        }
        is Null -> ""
        else -> throw IllegalStateException()
    }
}

fun fnTan(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double =
    kotlin.math.tan((args[0] as Number).toDouble())

fun fnToBoolean(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean = args[0].toBoolean()

fun fnToByte(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Byte
    = when (val form = args[0]) {
    is String -> form.toByte()
    is Number -> form.toByte()
    is JsonPrimitive -> form.asByte
        else -> throw IllegalStateException()
    }

fun fnToChar(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Char
        = when (val form = args[0]) {
    is String -> if (form.isEmpty()) '\u0000' else form[0]
    is Number -> form.toChar()
    is JsonPrimitive -> form.asCharacter
            else -> throw IllegalStateException()
        }

fun fnToDouble(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Double
        = when (val form = args[0]) {
    is String -> form.toDouble()
    is Number -> form.toDouble()
    is JsonPrimitive -> form.asDouble
            else -> throw IllegalStateException()
        }

fun fnToFloat(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Float
        = when (val form = args[0]) {
    is String -> form.toFloat()
    is Number -> form.toFloat()
    is JsonPrimitive -> form.asFloat
            else -> throw IllegalStateException()
        }

fun fnToInt(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Int
        = when (val form = args[0]) {
    is String -> form.toInt()
    is Number -> form.toInt()
    is JsonPrimitive -> form.asInt
            else -> throw IllegalStateException()
        }

fun fnToJson(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): JsonObject
        = when (val form = args[0]) {
    is String -> JsonParser.parseString(form).asJsonObject
    is Map -> form.toJson(ctx)
    is JsonObject -> form
            else -> throw IllegalStateException()
}

fun fnToLong(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Long
        = when (val form = args[0]) {
    is String -> form.toLong()
    is Number -> form.toLong()
    is JsonPrimitive -> form.asLong
            else -> throw IllegalStateException()
        }

fun fnToNumber(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number
        = when (val form = args[0]) {
    is String -> form.toDoubleOrNull() ?: form.toLong()
    is Number -> form.toLong()
    is JsonPrimitive -> form.asNumber
    else -> throw IllegalStateException()
}

fun fnToMap(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Map =
    jsonObjectToMap(ctx, args[0] as JsonObject)

fun jsonObjectToMap(ctx: Ctx, jsonObject: JsonObject): Map {
    val pairs = mutableMapOf<Any, Any>()
    val entrySet = jsonObject.entrySet()

    entrySet.forEach { entry ->
        val key = when (val keyForm = fnParse(ctx, arrayOf(entry.key), signatureIndex = 0)) {
            is Symbol -> keyForm.name
            else -> keyForm
        }
        val value = when (val jsonElement = entry.value) {
            is JsonPrimitive -> convertJsonPrimitiveToForm(jsonElement)
            is JsonArray -> jsonArrayToArray(ctx, jsonElement)
            is JsonObject -> jsonObjectToMap(ctx, jsonElement)
            else -> throw UnsupportedJsonElementException(ctx, jsonElement)
        }
        pairs[key] = value
    }

    return ImmutableMap.create(pairs)
}

fun jsonArrayToArray(ctx: Ctx, jsonArray: JsonArray): Array {
    val elements = mutableListOf<Any>()

    jsonArray.forEach { jsonElement ->
        val element = when (jsonElement) {
            is JsonPrimitive -> convertJsonPrimitiveToForm(jsonElement)
            is JsonArray -> jsonArrayToArray(ctx, jsonElement)
            is JsonObject -> jsonObjectToMap(ctx, jsonElement)
            else -> throw UnsupportedJsonElementException(ctx, jsonElement)
        }
        elements.add(element)
    }
    return ImmutableArray.create(elements)
}

private fun convertJsonPrimitiveToForm(primitive: JsonPrimitive): Any {
    return when {
        primitive.isBoolean -> primitive.asBoolean
        primitive.isNumber -> primitive.asNumber
        primitive.isString -> {
            val string = primitive.asString

            when {
                string.startsWith(":") -> Flx.getKeyword(string.substring(1))
                string.startsWith("'") && string.startsWith("'") -> CharToken.parseChar(string) ?: string
                else -> string
            }
        }
        else -> throw IllegalStateException()
    }
}

fun fnToShort(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Short
        = when (val form = args[0]) {
    is String -> form.toShort()
    is Number -> form.toShort()
    is JsonPrimitive -> form.asShort
            else -> throw IllegalStateException()
        }

fun fnToArray(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Array =
    when (val form = args[0]) {
        is String -> form.toArray()
        is List -> form.toArray()
        is Array -> form
        is Map -> form.toArray()
        is Set -> form.toArray()
        else -> ImmutableArray.create(listOf(form))
    }

fun fnToList(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): List =
    when (val form = args[0]) {
        is String -> form.toList()
        is List -> form
        is Array -> form.toList()
        is Map -> form.toList()
        is Set -> form.toList()
        else -> ImmutableList.create(listOf(form))
    }

fun fnToSet(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Set =
    when (val form = args[0]) {
        is String -> form.toSet()
        is List -> form.toSet()
        is Array -> form.toSet()
        is Map -> form.toSet()
        is Set -> form
        else -> ImmutableSet.create(setOf(form))
    }

fun fnToString(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String = args[0].toString()

fun fnTrim(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String
        = (args[0] as String).trim()

fun fnTrimStart(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String
        = (args[0] as String).trimStart()

fun fnTrimEnd(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String
        = (args[0] as String).trimEnd()

fun fnToUpperCase(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): String = (args[0] as String).toUpperCase(
    Locale.ENGLISH
)

fun fnXor(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Boolean {
    var count = 0
    for (arg in args) if (arg.isTrue()) count++
    return count == 1
}
