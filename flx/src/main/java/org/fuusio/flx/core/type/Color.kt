package org.fuusio.flx.core.type

import org.fuusio.flx.core.EvaluableObject

data class Color(val colorInt: Int) : EvaluableObject() {

    constructor(red: Int, green: Int, blue: Int, alpha: Int = 255) :
            this(android.graphics.Color.argb(alpha, red, green, blue))

    fun getAlpha() = android.graphics.Color.alpha(colorInt)

    fun getRed() = android.graphics.Color.red(colorInt)

    fun getGreen() = android.graphics.Color.green(colorInt)

    fun getBlue() = android.graphics.Color.blue(colorInt)

    override fun isColor() = false

    override fun toLiteral(): String {
        val red = String.format("%02X", (0xff and getRed()))
        val green = String.format("%02X", (0xff and getGreen()))
        val blue = String.format("%02X", (0xff and getBlue()))
        val alpha = String.format("%02X", (0xff and getAlpha()))
        return "#$alpha$red$green$blue"
    }

    companion object {
        val hilla = Color(0xfe, 0x3f, 0x00)
        val white = Color(0xff, 0xff, 0xff)
        val black = Color(0x00, 0x00, 0x00)
        val blueGray050 = Color(0xEC, 0xEF, 0xF1)
        val blueGray300 = Color(0x90, 0xA4, 0xAE)
        val blueGray600 = Color(0x54, 0x6E, 0x7A)
        val blueGray800 = Color(0x37, 0x47, 0x4F)
        val transparent = Color(0x00, 0x00, 0x00, alpha = 0x00)
    }

    /*
        BLUE_GREY_050("Blue Grey 050", 0xECEFF1),
    BLUE_GREY_100("Blue Grey 100", 0xCFD8DC),
    BLUE_GREY_200("Blue Grey 200", 0xB0BBC5),
    BLUE_GREY_300("Blue Grey 300", 0x90A4AE),
    BLUE_GREY_400("Blue Grey 400", 0x78909C),
    BLUE_GREY_500("Blue Grey 500", 0x607D8B),
    BLUE_GREY_600("Blue Grey 600", 0x546E7A),
    BLUE_GREY_700("Blue Grey 700", 0x455A64),
    BLUE_GREY_800("Blue Grey 800", 0x37474F),
    BLUE_GREY_900("Blue Grey 900", 0x263238);
     */
}