package org.fuusio.api.extension

fun Int.hasBits(bitMask: Int) = this and bitMask != 0