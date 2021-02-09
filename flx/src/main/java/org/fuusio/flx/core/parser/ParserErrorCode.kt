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
package org.fuusio.flx.core.parser

enum class ParserErrorCode(private val messageFormat: String) {
    FAILED_TO_A_FORM("Failed to parse a form at %1d"),
    FAILED_TO_PARSE_ARRAY("Failed to parse an array at %1d"),
    FAILED_TO_PARSE_BASE_INTEGER("Failed to parse base integer at %1d"),
    FAILED_TO_PARSE_CHAR("Failed to parse char at %1d"),
    FAILED_TO_PARSE_COMMENT("Failed to parse comment at %1d"),
    FAILED_TO_PARSE_DOUBLE("Failed to parse double at %1d"),
    FAILED_TO_PARSE_HEXADECIMAL("Failed to parse hexadecimal at %1d"),
    FAILED_TO_PARSE_KEYWORD("Failed to parse keyword at %1d"),
    FAILED_TO_PARSE_LIST("Failed to parse a list at %1d"),
    FAILED_TO_PARSE_LONG("Failed to parse long at %1d"),
    FAILED_TO_PARSE_MAP("Failed to parse a map at %1d"),
    FAILED_TO_PARSE_REGEX("Failed to parse regex at %1d"),
    FAILED_TO_PARSE_REGEX_OR_SET("Failed to parse regex or set at %1d"),
    FAILED_TO_PARSE_SET("Failed to parse a set at %1d"),
    FAILED_TO_PARSE_STRING("Failed to parse string at %1d"),
    FAILED_TO_PARSE_SYMBOL("Failed to parse symbol at %1d"),
    INVALID_BASE_INTEGER("Invalid base integer: '%1s'"),
    INVALID_CHAR("Invalid char: '%1s'"),
    INVALID_DOUBLE("Invalid double: '%1s'"),
    INVALID_HEXADECIMAL("Invalid double: '%1s'"),
    INVALID_LONG("Invalid long: '%1s'"),
    UNKNOWN_CHAR("Parsing failed because of unknown character %1s"),
    UNKNOWN_ERROR("Unknown parsing error at %1d");

    fun formatDescription(vararg args: Any) = messageFormat.format(*args)
}