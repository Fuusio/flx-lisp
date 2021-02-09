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
@file:Suppress("unused")

package org.fuusio.flx.core.function

import org.fuusio.flx.Flx
import org.fuusio.flx.R
import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.ArgError
import org.fuusio.flx.core.error.InvalidNumberOfArgsException
import org.fuusio.flx.core.type.ArgModifier
import org.fuusio.flx.core.type.SignatureSpec
import org.fuusio.flx.core.type.CoreType
import org.fuusio.flx.core.type.CoreType.*
import org.fuusio.flx.core.vm.Ctx

enum class FunctionSpec(
    override val symbol: String,
    override val function: Function,
    override val outputType: CoreType,
    override val signatureSpecs: Array<SignatureSpec>,
    override val style: FunctionStyle = FunctionStyle.LISP_FUNCTION,
    override val searchKeywords: String = "",
): FunctionFormSpec {

    GET_CLASS("getClass", ::fnGetClass, CLASS,
        signatures {
            signature {
                description(R.string.desc_get_class)
                arg(ANY, NAME_OBJECT)
            }
        }
    ),

    CLASS_FOR_NAME("Class.forName", ::fnClassForName, CLASS,
        signatures {
            signature {
                description(R.string.desc_class_for_name)
                arg(ANY, NAME_CLASS_NAME)
            }
        }
    ),

    IS_INSTANCE("isInstance", ::fnIsInstance, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_instance)
                arg(ANY, NAME_OBJECT)
                arg(ANY, NAME_CLASS)
            }
        }
    ),

    IS("is", ::fnIsInstance, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_instance)
                arg(ANY, NAME_OBJECT)
                arg(ANY, NAME_CLASS)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION
    ),

    LOG_D("Log.d", ::fnLogD, NULL,
        signatures {
            signature {
                description(R.string.desc_log_d)
                arg(ANY, NAME_MESSAGE)
            }
        }
    ),

    LOG_I("Log.i", ::fnLogI, NULL,
        signatures {
            signature {
                description(R.string.desc_log_i)
                arg(ANY, NAME_MESSAGE)
            }
        }
    ),

    LOG_W("Log.w", ::fnLogW, NULL,
        signatures {
            signature {
                description(R.string.desc_log_w)
                arg(ANY, NAME_MESSAGE)
            }
        }
    ),

    LOG_E("Log.e", ::fnLogE, NULL,
        signatures {
            signature {
                description(R.string.desc_log_e)
                arg(ANY, NAME_MESSAGE)
            }
        }
    ),

    ADDITION("+", ::fnAddition, NUMBER,
        signatures {
            signature {
                description(R.string.desc_addition)
                arg(NUMBER, NAME_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    PERCENTAGE("%", ::fnPercentage, NUMBER,
        signatures {
            signature {
                description(R.string.desc_percentage)
                arg(NUMBER, NAME_PERCENTAGE)
                arg(NUMBER, NAME_TOTAL)
            }
        }
    ),

    SUBTRACTION("-", ::fnSubtraction, NUMBER,
        signatures {
            signature {
                description(R.string.desc_subtraction)
                arg(NUMBER, NAME_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    MULTIPLICATION("*", ::fnMultiplication, NUMBER,
        signatures {
            signature {
                description(R.string.desc_multiplication)
                arg(NUMBER, NAME_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    DIVISION("/", ::fnDivision, NUMBER,
        signatures {
            signature {
                description(R.string.desc_division)
                arg(NUMBER, NAME_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    EQUAL("==", ::fnEqual, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_equals)
                    arg(ANY, NAME_FORM)
                    arg(ANY, NAME_EQ_FORM,ArgModifier.VARARG)
            }
        }
    ),

    GREATER_THAN(">", ::fnGreaterThan, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_greater_than)
                arg(NUMBER, NAME_NUMBER)
                arg(NUMBER, NAME_GT_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    GREATER_THAN_OR_EQUAL(">=", ::fnGreaterThanOrEqual, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_greater_than_or_equal)
                arg(NUMBER, NAME_NUMBER)
                arg(NUMBER, NAME_GT_EQ_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    INVOKE("invoke", ::fnInvoke, ANY,
        signatures {
            signature {
                description(R.string.desc_invoke)
                arg(ANY, NAME_OBJECT)
                arg(STRING, NAME_METHOD)
                arg(ANY, NAME_PARAMS, ArgModifier.VARARG)
            }
        }
    ),

    GET_PROPERTY("getProperty", ::fnGetProperty, ANY,
        signatures {
            signature {
                description(R.string.desc_get_property)
                arg(ANY, NAME_OBJECT)
                arg(STRING, NAME_PROPERTY)
            }
        }
    ),

    SET_PROPERTY("setProperty", ::fnSetProperty, ANY,
        signatures {
            signature {
                description(R.string.desc_set_property)
                arg(ANY, NAME_OBJECT)
                arg(STRING, NAME_PROPERTY)
                arg(ANY, NAME_VALUE)
            }
        }
    ),

    CLASS_NEW("Class.new", ::fnClassNew, ANY,
        signatures {
            signature {
                description(R.string.desc_new)
                arg(CLASS, NAME_CLASS)
                arg(ANY, NAME_PARAMS, ArgModifier.VARARG)
            }
        }
    ),

    LESSER_THAN("<", ::fnLesserThan, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_lesser_than)
                    arg(NUMBER, NAME_NUMBER)
                    arg(NUMBER, NAME_LT_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    LESSER_THAN_OR_EQUAL("<=", ::fnLesserThanOrEqual, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_lesser_than_or_equal)
                    arg(NUMBER, NAME_NUMBER)
                    arg(NUMBER, NAME_LT_EQ_NUMBERS,ArgModifier.VARARG)
            }
        }
    ),

//    EQUAL("==", ::fnEqual, BOOLEAN, R.string.desc_equal,
//        signatures(
//            signature {
//                arg(NUMBER, NAME_NUMBER),
//                arg(NUMBER, NAME_NUMBERS, ArgModifier.VARARG)
//            )
//        )),

    ABS("abs", ::fnAbs, NUMBER,
        signatures {
            signature {
                description(R.string.desc_abs)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    ACOS("acos", ::fnAcos, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_acos)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    AND("and", ::fnAnd, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_and)
                arg(ANY, NAME_FORMS, ArgModifier.VARARG)
            }
        }
    ),

    IS_ARRAY("isArray", ::fnIsArray, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_array)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    ASIN("asin", ::fnAsin, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_asin)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    ATAN("atan", ::fnAtan, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_atan)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    BLANK("isBlank", ::fnIsBlank, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_blank)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    BIT_AND("bitAnd", ::fnBitAnd, LONG,
        signatures {
            signature {
                description(R.string.desc_bit_and)
                arg(LONG, NAME_INT)
                arg(LONG, NAME_INT)
                arg(LONG, NAME_INTS,ArgModifier.VARARG)
            }
        }
    ),

    BIT_NOT("bitNot", ::fnBitNot, LONG,
        signatures {
            signature {
                description(R.string.desc_bit_not)
                arg(LONG, NAME_INT)
            }
        }
    ),

    BIT_OR("bitOr", ::fnBitOr, LONG,
        signatures {
            signature {
                description(R.string.desc_bit_or)
                arg(LONG, NAME_INT)
                arg(LONG, NAME_INT)
                arg(LONG, NAME_INTS,ArgModifier.VARARG)
            }
        }
    ),

    SHL("shl", ::fnShl, LONG,
        signatures {
            signature {
                description(R.string.desc_shl)
                arg(LONG, NAME_INT)
                arg(LONG, NAME_N)
            }
        }
    ),

    SHR("shr", ::fnShr, LONG,
        signatures {
            signature {
                description(R.string.desc_shr)
                arg(LONG, NAME_INT)
                arg(LONG, NAME_N)
            }
        }
    ),

    BIT_XOR("bitXor", ::fnBitXor, LONG,
        signatures {
            signature {
                description(R.string.desc_bit_xor)
                    arg(LONG, NAME_INT)
                    arg(LONG, NAME_INT)
                    arg(LONG, NAME_INTS,ArgModifier.VARARG)
            }
        }
    ),

    IS_BOOLEAN("isBoolean", ::fnIsBoolean, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_boolean)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    IS_BYTE("isByte", ::fnIsByte, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_byte)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    BUT_LAST("butLast", ::fnButlast, STRING,
        signatures {
            signature {
                description(R.string.desc_butlast)
                arg(NUMBER, NAME_N)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    @ExperimentalStdlibApi
    CAPITALIZE("capitalize", ::fnCapitalize, STRING,
        signatures {
            signature {
                description(R.string.desc_capitalize)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    CHOP("chop", ::fnChop, STRING,
        signatures {
            signature {
                description(R.string.desc_chop)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    CEIL("ceil", ::fnCeil, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_ceil)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    COLOR("color", ::fnColor, CoreType.COLOR,
        signatures {
            signature {
                description(R.string.desc_color)
                arg(NUMBER, NAME_RED)
                arg(NUMBER, NAME_GREEN)
                arg(NUMBER, NAME_BLUE)
                arg(NUMBER, NAME_ALPHA, ArgModifier.OPTIONAL)
            }
        }
    ),

    COMPARE("compare", ::fnCompare, LONG,
        signatures {
            signature {
                description(R.string.desc_compare)
                    arg(STRING, NAME_STRING_1)
                    arg(STRING, NAME_STRING_2)
            }
        }
    ),

    CONJ("conj", ::fnConj, ANY,
        signatures {
            signature {
                description(R.string.desc_conj_0)
                arg(SEQUENCE, NAME_COLLECTION)
                arg(ANY, NAME_ITEM)
            }
            signature {
                description(R.string.desc_conj_1)
                arg(SET, NAME_COLLECTION)
                arg(ANY, NAME_ITEM)
            }
        }
    ),

    CONS("cons", ::fnCons, LIST,
        signatures {
            signature {
                description(R.string.desc_cons_0)
                arg(ANY, NAME_ITEM)
                arg(SEQUENCE, NAME_SEQUENCE)
            }
            signature {
                description(R.string.desc_cons_1)
                arg(ANY, NAME_ITEM)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_cons_2)
                arg(ANY, NAME_ITEM)
                arg(SET, NAME_SET)
            }
        }
    ),

    COS("cos", ::fnCos, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_cos)
                    arg(NUMBER, NAME_X)
            }
        }
    ),

    DATE("date", ::fnDate, CoreType.DATE,
        signatures {
            signature {
                description(R.string.desc_date)
            }
        }
    ),

    GET_YEAR(".year", ::fnGetYear, LONG,
        signatures {
            signature {
                description(R.string.desc_date_year)
                arg(CoreType.DATE, NAME_DATE)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Date",
    ),

    GET_MONTH(".month", ::fnGetMonth, LONG,
        signatures {
            signature {
                description(R.string.desc_date_month)
                arg(CoreType.DATE, NAME_DATE)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Date",
    ),

    GET_DAY(".day", ::fnGetDay, LONG,
        signatures {
            signature {
                description(R.string.desc_date_day_of_month)
                arg(CoreType.DATE, NAME_DATE)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Date",
    ),

    GET_HOURS(".hour", ::fnGetHour, LONG,
        signatures {
            signature {
                description(R.string.desc_date_hours)
                    arg(CoreType.DATE, NAME_DATE)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Date",
    ),

    GET_MINUTES(".minute", ::fnGetMinute, LONG,
        signatures {
            signature {
                description(R.string.desc_date_minutes)
                    arg(CoreType.DATE, NAME_DATE)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Date",
    ),

    GET_SECONDS(".second", ::fnGetSecond, LONG,
        signatures {
            signature {
                description(R.string.desc_date_seconds)
                    arg(CoreType.DATE, NAME_DATE)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Date",
    ),

    GET_MILLISECONDS(".millisecond", ::fnGetMillisecond, LONG,
        signatures {
            signature {
                description(R.string.desc_date_seconds)
                arg(CoreType.DATE, NAME_DATE)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Date",
    ),

    DEC("dec", ::fnDec, NUMBER,
        signatures {
            signature {
                description(R.string.desc_cos)
                    arg(NUMBER, NAME_NUMBER)
            }
        }
    ),

    IS_DOUBLE("isDouble", ::fnIsDoubleQ, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_double)
                    arg(ANY, NAME_FORM)
            }
        }
    ),

    DROP("drop", ::fnDrop, STRING,
        signatures {
            signature {
                description(R.string.desc_drop)
                    arg(NUMBER, NAME_N)
                    arg(STRING, NAME_STRING)
            }
        }
    ),

    DROP_LAST("dropLast", ::fnDropLast, STRING,
        signatures {
            signature {
                description(R.string.desc_drop_last)
                arg(NUMBER, NAME_N)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    ENDS_WITH("endsWith", ::fnEndsWith, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_ends_with)
                    arg(STRING, NAME_STRING)
                    arg(STRING, NAME_POSTFIX)
            }
        }
    ),

    EPOCH_TIME("epochTime", ::fnEpochTime, LONG,
        signatures {
            signature {
                description(R.string.desc_epoch_time)
            }
        }
    ),

    EVAL("eval", ::fnEval, ANY,
        signatures {
            signature {
                description(R.string.desc_eval)
                    arg(ANY, NAME_FORM)
            }
        }
    ),

    EVAL_STRING("evalString", ::fnEvalString, ANY,
        signatures {
            signature {
                description(R.string.desc_eval_string)
                    arg(STRING, NAME_STRING)
            }
        }
    ),

    IS_EVEN("isEven", ::fnIsEven, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_even)
                    arg(LONG, NAME_INTEGER)
            }
        }
    ),

    EXP("exp", ::fnExp, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_exp)
                    arg(NUMBER, NAME_X)
            }
        }
    ),

    IS_FALSE("isFalse", ::fnIsFalse, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_false)
                    arg(ANY, NAME_FORM)
            }
        }
    ),

    FILTER("filter", ::fnFilter, LIST,
        signatures {
            signature {
                description(R.string.desc_filter_0)
                arg(FUNCTION, NAME_PREDICATE)
                arg(SEQUENCE, NAME_COLLECTION)
            }
            signature {
                description(R.string.desc_filter_1)
                arg(FUNCTION, NAME_PREDICATE)
                arg(SET, NAME_COLLECTION)
            }
        }
    ),

    FIRST("first", ::fnFirst, ANY,
        signatures {
            signature {
                description(R.string.desc_first_0)
                arg(SEQUENCE, NAME_SEQUENCE)
            }
            signature {
                description(R.string.desc_first_1)
                arg(STRING, NAME_SEQUENCE)
            }
        }
    ),

    FLOOR("floor", ::fnFloor, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_floor)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    FORMAT("format", ::fnFormat, NULL,
        signatures {
            signature {
                description(R.string.desc_format)
                arg(ANY, NAME_FORM)
                arg(ANY, NAME_FORMAT_ARGS, ArgModifier.VARARG)
            }
        }
    ),

    GET("get", ::fnGet, ANY,
        signatures {
            signature {
                description(R.string.desc_get_0)
                arg(JSON_OBJECT, NAME_JSON)
                arg(ANY, NAME_KEY)
            }
            signature {
                description(R.string.desc_get_1)
                arg(JSON_OBJECT, NAME_JSON)
                arg(ANY, NAME_KEY)
                arg(ANY, NAME_FALLBACK)
            }
            signature {
                description(R.string.desc_get_2)
                arg(MAP, NAME_MAP)
                arg(ANY, NAME_KEY)
            }
            signature {
                description(R.string.desc_get_3)
                arg(MAP, NAME_MAP)
                arg(ANY, NAME_KEY)
                arg(ANY, NAME_FALLBACK)
            }
        }
    ),

    JSON_GET(".get", ::fnJsonGet, ANY,
        signatures {
            signature {
                description(R.string.desc_json_get_0)
                arg(JSON_OBJECT, NAME_JSON)
                arg(ANY, NAME_KEY)
            }
            signature {
                description(R.string.desc_json_get_1)
                arg(JSON_OBJECT, NAME_JSON)
                arg(ANY, NAME_KEY)
                arg(ANY, NAME_FALLBACK)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "JSON"
    ),


    IDENTICAL("===", ::fnIdentical, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_identical)
                arg(ANY, NAME_FORM_1)
                arg(ANY, NAME_FORM_2)
            }
        }
    ),

    INC("inc", ::fnInc, NUMBER,
        signatures {
            signature {
                description(R.string.desc_inc)
                arg(NUMBER, NAME_NUMBER)
            }
        }
    ),

    INDEX_OF("indexOf", ::fnIndexOf, LONG,
        signatures {
            signature {
                description(R.string.desc_index_of)
                arg(STRING, NAME_STRING)
                arg(STRING, NAME_SUBSTRING)
                arg(LONG, NAME_INDEX)
            }
        }
    ),

    LAST("last", ::fnLast, ANY,
        signatures {
            signature {
                description(R.string.desc_last_0)
                arg(SEQUENCE, NAME_SEQUENCE)
            }
            signature {
                description(R.string.desc_last_1)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    IS_JSON_ARRAY("isJsonArray", ::fnIsJsonArray, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_json_array)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    IS_JSON_ELEMENT("isJsonElement", ::fnIsJsonElement, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_json_element)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    IS_JSON_OBJECT("isJsonObject", ::fnIsJsonObject, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_json_object)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    IS_JSON_PRIMITIVE("isJsonPrimitive", ::fnIsJsonPrimitive, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_json_primitive)
                arg(ANY, NAME_FORM)
            }
        }
    ),


    IS_LIST("isList", ::fnIsList, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_list)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    LOG("log", ::fnLog, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_log)
                arg(NUMBER, NAME_X)
                arg(NUMBER, NAME_BASE)
            }
        }
    ),

    IS_LONG("isLong", ::fnIsLong, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_long)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TO_LOWER_CASE("toLowerCase", ::fnToLowerCase, STRING,
        signatures {
            signature {
                description(R.string.desc_lower_case)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    IS_MAP("isMap", ::fnIsMap, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_map)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    MATCHES("matches", ::fnMatches, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_matches_0)
                arg(STRING, NAME_STRING)
                arg(REGEX, NAME_REGEX)
            }
            signature {
                description(R.string.desc_matches_1)
                arg(STRING, NAME_STRING)
                arg(STRING, NAME_REGEX)
            }
        }
    ),

    MAX("max", ::fnMax, NUMBER,
        signatures {
            signature {
                description(R.string.desc_max)
                arg(NUMBER, NAME_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    MIN("min", ::fnMin, NUMBER,
        signatures {
            signature {
                description(R.string.desc_min)
                arg(NUMBER, NAME_NUMBERS, ArgModifier.VARARG)
            }
        }
    ),

    IS_EMPTY("isEmpty", ::fnIsEmpty, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_empty_0)
                arg(SIZEABLE, NAME_COLLECTION)
            }
            signature {
                description(R.string.desc_is_empty_1)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    IS_NOT_EMPTY("isNotEmpty", ::fnIsNotEmpty, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_not_empty_0)
                arg(SIZEABLE, NAME_COLLECTION)
            }
            signature {
                description(R.string.desc_is_not_empty_1)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    IS_NEGATIVE("isNegative", ::fnIsNegative, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_negative)
                arg(NUMBER, NAME_NUMBER)
            }
        }
    ),

    IS_NULL("isNull", ::fnIsNull, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_null)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    NOT("not", ::fnNot, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_not)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    NOT_EQUAL("!=", ::fnNotEqual, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_not_equals)
                arg(ANY, NAME_FORM)
                arg(ANY, NAME_NOT_EQ_FORM, ArgModifier.VARARG)
            }
        }
    ),

    CHAR_AT("charAt", ::fnCharAt, CHAR,
        signatures {
            signature {
                description(R.string.desc_char_at)
                arg(STRING, NAME_STRING)
                arg(NUMBER, NAME_INDEX)
            }
        }
    ),

    NTH("nth", ::fnNth, ANY,
        signatures {
            signature {
                description(R.string.desc_nth)
                arg(INDEXABLE, NAME_SEQUENCE)
                arg(NUMBER, NAME_INDEX)
            }
        }
    ),

    IS_ODD("isOdd", ::fnIsOdd, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_odd)
                arg(LONG, NAME_INTEGER)
            }
        }
    ),

    OR("or", ::fnOr, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_or)
                arg(ANY, NAME_FORMS, ArgModifier.VARARG)
            }
        }
    ),

    IS_POS("isPositive", ::fnIsPositive, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_positive)
                arg(NUMBER, NAME_NUMBER)
            }
        }
    ),

    PARSE_BOOLEAN("parseBoolean", ::fnParseBoolean, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_parse_boolean)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    PARSE_BYTE("parseByte", ::fnParseByte, ANY,
        signatures {
            signature {
                description(R.string.desc_parse_byte)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    PARSE_DOUBLE("parseDouble", ::fnParseDouble, ANY,
        signatures {
            signature {
                description(R.string.desc_parse_double)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    PARSE_FLOAT("parseFloat", ::fnParseFloat, ANY,
        signatures {
            signature {
                description(R.string.desc_parse_float)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    PARSE_INT("parseInt", ::fnParseInt, ANY,
        signatures {
            signature {
                description(R.string.desc_parse_int)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    PARSE_LONG("parseLong", ::fnParseLong, ANY,
        signatures {
            signature {
                description(R.string.desc_parse_long)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    POW("pow", ::fnPow, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_pow)
                arg(NUMBER, NAME_X)
                arg(NUMBER, NAME_Y)
            }
        }
    ),

    PRINT("print", ::fnPrint, NULL,
        signatures {
            signature {
                description(R.string.desc_print)
                arg(ANY, NAME_FORM)
                arg(ANY, NAME_FORMAT_ARGS, ArgModifier.VARARG)
            }
        }
    ),

    PRINTLN("println", ::fnPrintln, NULL,
        signatures {
            signature {
                description(R.string.desc_println)
                arg(ANY, NAME_FORM)
                arg(ANY, NAME_FORMAT_ARGS, ArgModifier.VARARG)
            }
        }
    ),

    RAND("rand", ::fnRand, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_rand)
            }
        }
    ),

    PARSE("parse", ::fnParse, ANY,
        signatures {
            signature {
                description(R.string.desc_parse_string)
                arg(STRING, NAME_LITERAL)
            }
        }
    ),

    STRING_EVAL(".eval", ::fnParseEval, ANY,
        signatures {
            signature {
                description(R.string.desc_parse_eval_string)
                arg(STRING, NAME_LITERAL)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION
    ),

    COMPILE(".compile", ::fnCompile, PARSED_FORM,
        signatures {
            signature {
                description(R.string.desc_parse_eval_string)
                arg(STRING, NAME_LITERAL)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION
    ),

    REM("rem", ::fnRem, NUMBER,
        signatures {
            signature {
                description(R.string.desc_rem)
                arg(NUMBER, NAME_NUMBER)
                arg(NUMBER, NAME_DIVIDER)
            }
        }
    ),

    REPEAT_STRING("repeatString", ::fnRepeatString, STRING,
        signatures {
            signature {
                description(R.string.desc_repeat_string)
                arg(LONG, NAME_N)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    REPLACE_CHAR("replaceChar", ::fnReplaceChar, STRING,
        signatures {
            signature {
                description(R.string.desc_replace_char)
                arg(STRING, NAME_STRING)
                arg(CHAR, NAME_OLD_CHAR)
                arg(CHAR, NAME_NEW_CHAR)
            }
        }
    ),

    REPLACE_FIRST_REGEX("replaceFirstRegex", ::fnReplaceFirstRegex, STRING,
        signatures {
            signature {
                description(R.string.desc_replace_first_re_0)
                arg(REGEX, NAME_REGEX)
                arg(STRING, NAME_REPLACEMENT)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_replace_first_re_1)
                arg(STRING, NAME_REGEX)
                arg(STRING, NAME_REPLACEMENT)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    REPLACE_FIRST_STRING("replaceFirstString", ::fnReplaceFirstString, STRING,
        signatures {
            signature {
                description(R.string.desc_replace_first_str)
                arg(STRING, NAME_SUBSTRING)
                arg(STRING, NAME_REPLACEMENT)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    REPLACE_REGEX("replaceRegex", ::fnReplaceRegex, STRING,
        signatures {
            signature {
                description(R.string.desc_replace_re_0)
                arg(REGEX, NAME_REGEX)
                arg(STRING, NAME_REPLACEMENT)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_replace_re_1)
                arg(STRING, NAME_REGEX)
                arg(STRING, NAME_REPLACEMENT)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    REPLACE_STRING("replaceString", ::fnReplaceString, STRING,
        signatures {
            signature {
                description(R.string.desc_replace_str)
                arg(STRING, NAME_SUBSTRING)
                arg(STRING, NAME_REPLACEMENT)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    REST("rest", ::fnRest, SEQUENCE,
        signatures {
            signature {
                description(R.string.desc_rest_0)
                arg(SEQUENCE, NAME_SEQUENCE)
            }
            signature {
                description(R.string.desc_rest_1)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    REVERSE("reverse", ::fnReverse, STRING,
        signatures {
            signature {
                description(R.string.desc_reverse)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    ROUND("round", ::fnRound, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_round)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    IS_SET("isSet", ::fnIsSet, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_set)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    SIGN("sign", ::fnSign, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_sign)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    SQRT("sqrt", ::fnSqrt, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_sqrt)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    SIN("sin", ::fnSin, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_sin)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    SIZE("size", ::fnSize, LONG,
        signatures {
            signature {
                description(R.string.desc_size_0)
                arg(SIZEABLE, NAME_COLLECTION)
            }
            signature {
                description(R.string.desc_size_1)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    SPLIT("split", ::fnSplit, ARRAY,
        signatures {
            signature {
                description(R.string.desc_split_0)
                arg(REGEX, NAME_REGEX)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_split_1)
                arg(STRING, NAME_REGEX)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_split_2)
                arg(REGEX, NAME_REGEX)
                arg(LONG, NAME_LIMIT)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_split_3)
                arg(STRING, NAME_REGEX)
                arg(LONG, NAME_LIMIT)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    SPLIT_LINES("splitLines", ::fnSplitLines, ARRAY,
        signatures {
            signature {
                description(R.string.desc_split_lines)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    STARTS_WITH("startsWith", ::fnStartsWith, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_starts_with)
                arg(STRING, NAME_STRING)
                arg(STRING, NAME_PREFIX)
            }
        }
    ),

    CONC("conc", ::fnStr, STRING,
        signatures {
            signature {
                description(R.string.desc_concatenate)
                arg(ANY, NAME_FORM, ArgModifier.VARARG)
            }
        }
    ),

    STR("str", ::fnStr, STRING,
        signatures {
            signature {
                description(R.string.desc_str)
                arg(ANY, NAME_FORM, ArgModifier.VARARG)
            }
        }
    ),

    IS_STRING("isString", ::fnIsString, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_string)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    SUB_ARRAY("subArray", ::fnSubArray, ARRAY,
        signatures {
            signature {
                description(R.string.desc_subarray_0)
                arg(ARRAY, NAME_ARRAY)
                arg(LONG, NAME_START)
                }
            signature {
                description(R.string.desc_subarray_1)
                arg(ARRAY, NAME_ARRAY)
                arg(LONG, NAME_START)
                arg(LONG, NAME_END)
            }
        }
    ),

    SUBSCRIBE(".subscribe", ::fnOnbservableSubscribe, SUBSCRIPTION,
        signatures {
            signature {
                description(R.string.desc_observer__subscribe)
                arg(OBSERVABLE, NAME_OBSERVABLE)
                arg(CALLBACK, NAME_OBSERVER)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Observable,Subscription"
    ),

    UNSUBSCRIBE(".unsubscribe", ::fnSubscriptionUnsubscribe, OBSERVABLE,
        signatures {
            signature {
                description(R.string.desc_subscription__unsubscribe)
                arg(SUBSCRIPTION, NAME_SUBSCRIPTION)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Observable,Subscription"
    ),

    TIMER_OBSERVABLE("timerObservable", ::fnTimerObservable, OBSERVABLE,
        signatures {
            signature {
                description(R.string.desc_timer_observable)
                arg(NUMBER, NAME_PERIOD)
            }
        }
    ),

    PERIODIC_TIMER_OBSERVABLE("periodicTimerObservable", ::fnPeriodicTimerObservable, OBSERVABLE,
        signatures {
            signature {
                description(R.string.desc_periodic_timer_observable)
                arg(NUMBER, NAME_INITIAL_DELAY)
                arg(NUMBER, NAME_PERIOD)
            }
        }
    ),

    START_OBSERVING(".start", ::fnSubscritionStart, SUBSCRIPTION,
        signatures {
            signature {
                description(R.string.desc_subscription__start)
                arg(SUBSCRIPTION, NAME_SUBSCRIPTION)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Observable,Subscription"
    ),

    PAUSE_OBSERVING(".pause", ::fnSubscritionPause, SUBSCRIPTION,
        signatures {
            signature {
                description(R.string.desc_subscription__pause)
                arg(SUBSCRIPTION, NAME_SUBSCRIPTION)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Observable,Subscription"
    ),

    RESUME_OBSERVING(".resume", ::fnSubscritionResume, SUBSCRIPTION,
        signatures {
            signature {
                description(R.string.desc_subscription__resume)
                arg(SUBSCRIPTION, NAME_SUBSCRIPTION)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Observable,Subscription"
    ),

    STOP_OBSERVING(".stop", ::fnSubscritionStop, SUBSCRIPTION,
        signatures {
            signature {
                description(R.string.desc_subscription__stop)
                arg(SUBSCRIPTION, NAME_SUBSCRIPTION)
            }
        },
        style = FunctionStyle.KOTLIN_FUNCTION,
        searchKeywords = "Observable,Subscription"
    ),

    SUBSTRING("substring", ::fnSubstring, STRING,
        signatures {
            signature {
                description(R.string.desc_substring_0)
                arg(STRING, NAME_STRING)
                arg(LONG, NAME_START)
            }
            signature {
                description(R.string.desc_substring_1)
                arg(STRING, NAME_STRING)
                arg(LONG, NAME_START)
                arg(LONG, NAME_END)
            }
        }
    ),

    CONTAINS("contains", ::fnContains, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_contains_0)
                arg(STRING, NAME_SUBSTRING)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_contains_1)
                arg(STRING, NAME_SUBSTRING)
                arg(STRING, NAME_STRING)
                arg(BOOLEAN, NAME_IS_CASE_IGNORED)
            }
        }
    ),

    IS_SYMBOL("isSymbol", ::fnIsSymbol, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_symbol)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TAIL("tail", ::fnTail, STRING,
        signatures {
            signature {
                description(R.string.desc_tail)
                arg(NUMBER, NAME_N)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    TAKE("take", ::fnTake, STRING,
        signatures {
            signature {
                description(R.string.desc_take)
                arg(NUMBER, NAME_N)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    TAN("tan", ::fnTan, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_tan)
                arg(NUMBER, NAME_X)
            }
        }
    ),

    TO_ARRAY("toArray", ::fnToArray, ARRAY,
        signatures {
            signature {
                description(R.string.desc_to_array)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TO_BOOLEAN("toBoolean", ::fnToBoolean, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_to_boolean)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TO_BYTE("toByte", ::fnToByte, BYTE,
        signatures {
            signature {
                description(R.string.desc_to_byte_0)
                arg(NUMBER, NAME_NUMBER)
            }
            signature {
                description(R.string.desc_to_byte_1)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_byte_2)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_CHAR("toChar", ::fnToChar, CHAR,
        signatures {
            signature {
                description(R.string.desc_to_char_0)
                arg(ANY, NAME_FORM)
            }
            signature {
                description(R.string.desc_to_char_1)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_DOUBLE("toDouble", ::fnToDouble, DOUBLE,
        signatures {
            signature {
                description(R.string.desc_to_double_0)
                arg(NUMBER, NAME_NUMBER)
            }
            signature {
                description(R.string.desc_to_double_1)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_double_2)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_FLOAT("toFloat", ::fnToFloat, FLOAT,
        signatures {
            signature {
                description(R.string.desc_to_float_0)
                arg(NUMBER, NAME_NUMBER)
            }
            signature {
                description(R.string.desc_to_float_1)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_float_2)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_INT("toInt", ::fnToInt, INT,
        signatures {
            signature {
                description(R.string.desc_to_int_0)
                arg(NUMBER, NAME_NUMBER)
            }
            signature {
                description(R.string.desc_to_int_1)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_int_2)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_JSON("toJson", ::fnToJson, JSON_OBJECT,
        signatures {
            signature {
                description(R.string.desc_to_json_0)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_json_1)
                arg(MAP, NAME_MAP)
            }
        }
    ),

    TO_LIST("toList", ::fnToList, LIST,
        signatures {
            signature {
                description(R.string.desc_to_list)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TO_LONG("toLong", ::fnToLong, LONG,
        signatures {
            signature {
                description(R.string.desc_to_long_0)
                arg(NUMBER, NAME_NUMBER)
            }
            signature {
                description(R.string.desc_to_long_1)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_long_2)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_MAP("toMap", ::fnToMap, MAP,
        signatures {
            signature {
                description(R.string.desc_to_map)
                arg(JSON_OBJECT, NAME_JSON)
            }
        }
    ),

    TO_NUMBER("toNumber", ::fnToNumber, NUMBER,
        signatures {
            signature {
                description(R.string.desc_to_number_0)
                arg(NUMBER, NAME_NUMBER)
            }
            signature {
                description(R.string.desc_to_number_1)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_number_2)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_SET("toSet", ::fnToSet, SET,
        signatures {
            signature {
                description(R.string.desc_to_set)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TO_SHORT("toShort", ::fnToShort, SHORT,
        signatures {
            signature {
                description(R.string.desc_to_short_0)
                arg(NUMBER, NAME_NUMBER)
            }
            signature {
                description(R.string.desc_to_short_1)
                arg(STRING, NAME_STRING)
            }
            signature {
                description(R.string.desc_to_short_2)
                arg(JSON_PRIMITIVE, NAME_JSON_PRIMITIVE)
            }
        }
    ),

    TO_STRING("toString", ::fnToString, STRING,
        signatures {
            signature {
                description(R.string.desc_to_string)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TRIM("trim", ::fnTrim, STRING,
        signatures {
            signature {
                description(R.string.desc_trim)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    TRIM_START("trimStart", ::fnTrimStart, STRING,
        signatures {
            signature {
                description(R.string.desc_trimStart)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    TRIM_END("trimEnd", ::fnTrimEnd, STRING,
        signatures {
            signature {
                description(R.string.desc_trimEnd)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    IS_TRUE("isTrue", ::fnIsTrue, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_true)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    TO_UPPER_CASE("toUpperCase", ::fnToUpperCase, STRING,
        signatures {
            signature {
                description(R.string.desc_upper_case)
                arg(STRING, NAME_STRING)
            }
        }
    ),

    IS_ZERO("isZero", ::fnIsZero, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_is_zero)
                arg(NUMBER, NAME_NUMBER)
            }
        }
    ),

    XOR("xor", ::fnXor, BOOLEAN,
        signatures {
            signature {
                description(R.string.desc_xor)
                arg(ANY, NAME_FORMS, ArgModifier.VARARG)
            }
        }
    );

    override fun getId(): String = this.name

    override fun isMacroSpec(): Boolean = false

    override val functionName: Symbol?
        get() = Flx.getSymbol(symbol)

    override fun call(ctx: Ctx, args: Array<Any>): Any =
        Evaluator.evalFunction(ctx, this, args)

    override fun eval(ctx: Ctx): Any = this

    override fun hasArgs(): Boolean {
        signatureSpecs.forEach { signature -> if (signature.argSpecs.isNotEmpty()) return true }
        return false
    }

    override fun checkArgs(argValues: Array<Any>): SignatureSpec? {
        signatureSpecs.forEach { signatureSpec -> if (signatureSpec.checkArgs(argValues)) return signatureSpec }
        return null
    }

    override fun getArgError(ctx: Ctx, args: Array<Any>): ArgError {
        signatureSpecs.forEach { signatureSpec ->
            if (signatureSpec.checkArgsCount(args)) {
                val argError = signatureSpec.getArgError(args)
                if (argError != null) return argError
            }
        }
        throw InvalidNumberOfArgsException(ctx, symbol, args.size, signatureSpecs[0].getArgsCount())
    }

    companion object {

        fun internSymbols() {
            values().forEach { value ->
                val symbol = Flx.internSymbol(FunctionSymbol(value))
                Flx.ctx.set(symbol, value)
            }
        }

        fun get(symbol: String, id: String? = null): FunctionFormSpec? {
            if (id != null) {
                values().forEach { value -> if (value.name == id) return@get value }
            } else {
                values().forEach { value -> if (value.symbol == symbol) return@get value }
            }
            return null
        }
    }
}