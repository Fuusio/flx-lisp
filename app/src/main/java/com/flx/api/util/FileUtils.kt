package com.flx.api.util

fun isValidFileName(fileName: String): Boolean {
    if (fileName.isBlank()) return false
    fileName.forEach { char ->
        if (!isValidFatFilenameChar(char)) return false
    }
    return true
}

fun isValidFatFilenameChar(char: Char): Boolean {
    val byteValue = char.toByte()
    return if ((byteValue in 0x00..0x1f) || byteValue == 0x7F.toByte()) {
        false
    } else {
        when (char) {
            '"', '*', '/', ':', '<', '>', '?', '\\', '|' -> false
            else -> true
        }
    }
}