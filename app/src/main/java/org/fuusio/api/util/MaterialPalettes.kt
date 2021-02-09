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

package org.fuusio.api.util

import androidx.annotation.ColorInt

interface MaterialColorPalette {

    @ColorInt fun getColor(index: Int): Int
}

enum class RedPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    RED_050("Red 050", 0xFFEBEE),
    RED_100("Red 100", 0xFFCDD2),
    RED_200("Red 200", 0xEF9A9A),
    RED_300("Red 300", 0xE57373),
    RED_400("Red 400", 0xEF5350),
    RED_500("Red 500", 0xF44336),
    RED_600("Red 600", 0xE53935),
    RED_700("Red 700", 0xD32F2F),
    RED_800("Red 800", 0xC62828),
    RED_900("Red 900", 0xB71C1C),
    RED_A100("Red A100", 0xFF8A80),
    RED_A200("Red A200", 0xFF5252),
    RED_A400("Red A400", 0xFF1744),
    RED_A700("Red A700", 0xD50000);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class PinkPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    PINK_050("Pink 050", 0xFCE4EC),
    PINK_100("Pink 100", 0xF8BBD0),
    PINK_200("Pink 200", 0xF48FB1),
    PINK_300("Pink 300", 0xF06292),
    PINK_400("Pink 400", 0xEC407A),
    PINK_500("Pink 500", 0xE91E63),
    PINK_600("Pink 600", 0xD81B60),
    PINK_700("Pink 700", 0xC2185B),
    PINK_800("Pink 800", 0xAD1457),
    PINK_900("Pink 900", 0x880E4F),
    PINK_A100("Pink A100", 0xFF80AB),
    PINK_A200("Pink A200", 0xFF4081),
    PINK_A400("Pink A400", 0xF50057),
    PINK_A700("Pink A700", 0xC51162);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class PurplePalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    PURPLE_050("Purple 050", 0xF3E5F5),
    PURPLE_100("Purple 100", 0xE1BEE7),
    PURPLE_200("Purple 200", 0xCE93D8),
    PURPLE_300("Purple 300", 0xBA68C8),
    PURPLE_400("Purple 400", 0xAB47BC),
    PURPLE_500("Purple 500", 0x9C27B0),
    PURPLE_600("Purple 600", 0x8E24AA),
    PURPLE_700("Purple 700", 0x7B1FA2),
    PURPLE_800("Purple 800", 0x6A1B9A),
    PURPLE_900("Purple 900", 0x4A148C),
    PURPLE_A100("Purple A100", 0xEA80FC),
    PURPLE_A200("Purple A200", 0xE040FB),
    PURPLE_A400("Purple A400", 0xD500F9),
    PURPLE_A700("Purple A700", 0xAA00FF);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class DeepPurplePalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    DEEP_PURPLE_050("Deep Purple 050", 0xEDE7F6),
    DEEP_PURPLE_100("Deep Purple 100", 0xD1C4E9),
    DEEP_PURPLE_200("Deep Purple 200", 0xB39DDB),
    DEEP_PURPLE_300("Deep Purple 300", 0x9575CD),
    DEEP_PURPLE_400("Deep Purple 400", 0x7E57C2),
    DEEP_PURPLE_500("Deep Purple 500", 0x673AB7),
    DEEP_PURPLE_600("Deep Purple 600", 0x5E35B1),
    DEEP_PURPLE_700("Deep Purple 700", 0x512DA8),
    DEEP_PURPLE_800("Deep Purple 800", 0x4527A0),
    DEEP_PURPLE_900("Deep Purple 900", 0x311B92),
    DEEP_PURPLE_A100("Deep Purple A100", 0xB388FF),
    DEEP_PURPLE_A200("Deep Purple A200", 0x7C4DFF),
    DEEP_PURPLE_A400("Deep Purple A400", 0x651FFF),
    DEEP_PURPLE_A700("Deep Purple A700", 0x6200EA);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class IndigoPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    INDIGO_050("Indigo 050", 0xE8EAF6),
    INDIGO_100("Indigo 100", 0xC5CAE9),
    INDIGO_200("Indigo 200", 0x9FA8DA),
    INDIGO_300("Indigo 300", 0x7986CB),
    INDIGO_400("Indigo 400", 0x5C6BC0),
    INDIGO_500("Indigo 500", 0x3F51B5),
    INDIGO_600("Indigo 600", 0x3949AB),
    INDIGO_700("Indigo 700", 0x303F9F),
    INDIGO_800("Indigo 800", 0x283593),
    INDIGO_900("Indigo 900", 0x1A237E),
    INDIGO_A100("Indigo A100", 0x8C9EFF),
    INDIGO_A200("Indigo A200", 0x536DFE),
    INDIGO_A400("Indigo A400", 0x3D5AFE),
    INDIGO_A700("Indigo A700", 0x304FFE);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class BluePalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    BLUE_050("Blue 050", 0xE3F2FD),
    BLUE_100("Blue 100", 0xBBDEFB),
    BLUE_200("Blue 200", 0x90CAF9),
    BLUE_300("Blue 300", 0x64B5F6),
    BLUE_400("Blue 400", 0x42A5F5),
    BLUE_500("Blue 500", 0x2196F3),
    BLUE_600("Blue 600", 0x1E88E5),
    BLUE_700("Blue 700", 0x1976D2),
    BLUE_800("Blue 800", 0x1565C0),
    BLUE_900("Blue 900", 0x0D47A1),
    BLUE_A100("Blue A100", 0x82B1FF),
    BLUE_A200("Blue A200", 0x448AFF),
    BLUE_A400("Blue A400", 0x2979FF),
    BLUE_A700("Blue A700", 0x2962FF);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class LightBluePalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    LIGHT_BLUE_050("Light Blue_050", 0xE1F5FE),
    LIGHT_BLUE_100("Light Blue_100", 0xB3E5FC),
    LIGHT_BLUE_200("Light Blue_200", 0x81D4fA),
    LIGHT_BLUE_300("Light Blue_300", 0x4fC3F7),
    LIGHT_BLUE_400("Light Blue_400", 0x29B6FC),
    LIGHT_BLUE_500("Light Blue_500", 0x03A9F4),
    LIGHT_BLUE_600("Light Blue_600", 0x039BE5),
    LIGHT_BLUE_700("Light Blue_700", 0x0288D1),
    LIGHT_BLUE_800("Light Blue_800", 0x0277BD),
    LIGHT_BLUE_900("Light Blue_900", 0x01579B),
    LIGHT_BLUE_A100("Light Blue A100", 0x80D8FF),
    LIGHT_BLUE_A200("Light Blue A200", 0x40C4FF),
    LIGHT_BLUE_A400("Light Blue A400", 0x00B0FF),
    LIGHT_BLUE_A700("Light Blue A700", 0x0091EA);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class CyanPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    CYAN_050("Cyan 050", 0xE0F7FA),
    CYAN_100("Cyan 100", 0xB2EBF2),
    CYAN_200("Cyan 200", 0x80DEEA),
    CYAN_300("Cyan 300", 0x4DD0E1),
    CYAN_400("Cyan 400", 0x26C6DA),
    CYAN_500("Cyan 500", 0x00BCD4),
    CYAN_600("Cyan 600", 0x00ACC1),
    CYAN_700("Cyan 700", 0x0097A7),
    CYAN_800("Cyan 800", 0x00838F),
    CYAN_900("Cyan 900", 0x006064),
    CYAN_A100("Cyan A100", 0x84FFFF),
    CYAN_A200("Cyan A200", 0x18FFFF),
    CYAN_A400("Cyan A400", 0x00E5FF),
    CYAN_A700("Cyan A700", 0x00B8D4);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class TealPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    TEAL_050("Teal 050", 0xE0F2F1),
    TEAL_100("Teal 100", 0xB2DFDB),
    TEAL_200("Teal 200", 0x80CBC4),
    TEAL_300("Teal 300", 0x4DB6AC),
    TEAL_400("Teal 400", 0x26A69A),
    TEAL_500("Teal 500", 0x009688),
    TEAL_600("Teal 600", 0x00897B),
    TEAL_700("Teal 700", 0x00796B),
    TEAL_800("Teal 800", 0x00695C),
    TEAL_900("Teal 900", 0x004D40),
    TEAL_A100("Teal A100", 0xA7FFEB),
    TEAL_A200("Teal A200", 0x64FFDA),
    TEAL_A400("Teal A400", 0x1DE9B6),
    TEAL_A700("Teal A700", 0x00BFA5);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class GreenPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    GREEN_050("Green 050", 0xE8F5E9),
    GREEN_100("Green 100", 0xC8E6C9),
    GREEN_200("Green 200", 0xA5D6A7),
    GREEN_300("Green 300", 0x81C784),
    GREEN_400("Green 400", 0x66BB6A),
    GREEN_500("Green 500", 0x4CAF50),
    GREEN_600("Green 600", 0x43A047),
    GREEN_700("Green 700", 0x388E3C),
    GREEN_800("Green 800", 0x2E7D32),
    GREEN_900("Green 900", 0x1B5E20),
    GREEN_A100("Green A100", 0xB9F6CA),
    GREEN_A200("Green A200", 0x69F0AE),
    GREEN_A400("Green A400", 0x00E676),
    GREEN_A700("Green A700", 0x00C853);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class LightGreenPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    LIGHT_GREEN_050("Light Green 050", 0xF1F8E9),
    LIGHT_GREEN_100("Light Green 100", 0xDCEDC8),
    LIGHT_GREEN_200("Light Green 200", 0xC5E1A5),
    LIGHT_GREEN_300("Light Green 300", 0xAED581),
    LIGHT_GREEN_400("Light Green 400", 0x9CCC65),
    LIGHT_GREEN_500("Light Green 500", 0x8BC34A),
    LIGHT_GREEN_600("Light Green 600", 0x7CB342),
    LIGHT_GREEN_700("Light Green 700", 0x689F38),
    LIGHT_GREEN_800("Light Green 800", 0x558B2F),
    LIGHT_GREEN_900("Light Green 900", 0x33691E),
    LIGHT_GREEN_A100("Light Green A100", 0xCCFF90),
    LIGHT_GREEN_A200("Light Green A200", 0xB2FF59),
    LIGHT_GREEN_A400("Light Green A400", 0x76FF03),
    LIGHT_GREEN_A700("Light Green A700", 0x64DD17);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class LimePalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    LINE_050("Line 050", 0xF9FBE7),
    LINE_100("Line 100", 0xF0F4C3),
    LINE_200("Line 200", 0xE6EE9C),
    LINE_300("Line 300", 0xDCE775),
    LINE_400("Line 400", 0xD4E157),
    LINE_500("Line 500", 0xCDDC39),
    LINE_600("Line 600", 0xC0CA33),
    LINE_700("Line 700", 0xA4B42B),
    LINE_800("Line 800", 0x9E9D24),
    LINE_900("Line 900", 0x827717),
    LINE_A100("Lime A100", 0xF4FF81),
    LINE_A200("Lime A200", 0xEEFF41),
    LINE_A400("Lime A400", 0xC6FF00),
    LINE_A700("Lime A700", 0xAEEA00);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class YellowPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    YELLOW_050("Yellow 050", 0xFFFDE7),
    YELLOW_100("Yellow 100", 0xFFF9C4),
    YELLOW_200("Yellow 200", 0xFFF590),
    YELLOW_300("Yellow 300", 0xFFF176),
    YELLOW_400("Yellow 400", 0xFFEE58),
    YELLOW_500("Yellow 500", 0xFFEB3B),
    YELLOW_600("Yellow 600", 0xFDD835),
    YELLOW_700("Yellow 700", 0xFBC02D),
    YELLOW_800("Yellow 800", 0xF9A825),
    YELLOW_900("Yellow 900", 0xF57F17),
    YELLOW_A100("Yellow A100", 0xFFFF82),
    YELLOW_A200("Yellow A200", 0xFFFF00),
    YELLOW_A400("Yellow A400", 0xFFEA00),
    YELLOW_A700("Yellow A700", 0xFFD600);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class AmberPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    AMBER_050("Amber 050", 0xFFF8E1),
    AMBER_100("Amber 100", 0xFFECB3),
    AMBER_200("Amber 200", 0xFFE082),
    AMBER_300("Amber 300", 0xFFD54F),
    AMBER_400("Amber 400", 0xFFCA28),
    AMBER_500("Amber 500", 0xFFC107),
    AMBER_600("Amber 600", 0xFFB300),
    AMBER_700("Amber 700", 0xFFA000),
    AMBER_800("Amber 800", 0xFF8F00),
    AMBER_900("Amber 900", 0xFF6F00),
    AMBER_A100("Amber A100", 0xFFE57F),
    AMBER_A200("Amber A200", 0xFFD740),
    AMBER_A400("Amber A400", 0xFFC400),
    AMBER_A700("Amber A700", 0xFFAB00);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class OrangePalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    ORANGE_050("Orange 050", 0xFFF3E0),
    ORANGE_100("Orange 100", 0xFFE0B2),
    ORANGE_200("Orange 200", 0xFFCC80),
    ORANGE_300("Orange 300", 0xFFB74D),
    ORANGE_400("Orange 400", 0xFFA726),
    ORANGE_500("Orange 500", 0xFF9800),
    ORANGE_600("Orange 600", 0xFB8C00),
    ORANGE_700("Orange 700", 0xF57C00),
    ORANGE_800("Orange 800", 0xEF6C00),
    ORANGE_900("Orange 900", 0xE65100),
    ORANGE_A100("Orange A100", 0xFFD180),
    ORANGE_A200("Orange A200", 0xFFAB40),
    ORANGE_A400("Orange A400", 0xFF9100),
    ORANGE_A700("Orange A700", 0xFF6D00);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class DeepOrangePalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    DEEP_ORANGE_050("Deep Orange 050", 0xFBE9A7),
    DEEP_ORANGE_100("Deep Orange 100", 0xFFCCBC),
    DEEP_ORANGE_200("Deep Orange 200", 0xFFAB91),
    DEEP_ORANGE_300("Deep Orange 300", 0xFF8A65),
    DEEP_ORANGE_400("Deep Orange 400", 0xFF7043),
    DEEP_ORANGE_500("Deep Orange 500", 0xFF5722),
    DEEP_ORANGE_600("Deep Orange 600", 0xF4511E),
    DEEP_ORANGE_700("Deep Orange 700", 0xE64A19),
    DEEP_ORANGE_800("Deep Orange 800", 0xD84315),
    DEEP_ORANGE_900("Deep Orange 900", 0xBF360C),
    DEEP_ORANGE_A100("Deep Orange A100", 0xFF9E80),
    DEEP_ORANGE_A200("Deep Orange A200", 0xFF6E40),
    DEEP_ORANGE_A400("Deep Orange A400", 0xFF3D00),
    DEEP_ORANGE_A700("Deep Orange A700", 0xDD2600);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class BrownPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    BROWN_050("Brown 050", 0xEFEBE9),
    BROWN_100("Brown 100", 0xD7CCC8),
    BROWN_200("Brown 200", 0xBCAAA4),
    BROWN_300("Brown 300", 0xA1887F),
    BROWN_400("Brown 400", 0x8D6E63),
    BROWN_500("Brown 500", 0x795548),
    BROWN_600("Brown 600", 0x6D4C41),
    BROWN_700("Brown 700", 0x5D4037),
    BROWN_800("Brown 800", 0x4E342E),
    BROWN_900("Brown 900", 0x3E2723);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class GreyPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
    GREY_050("Grey 050", 0xFAFAFA),
    GREY_100("Grey 100", 0xF5F5F5),
    GREY_200("Grey 200", 0xEEEEEE),
    GREY_300("Grey 300", 0xE0E0E0),
    GREY_400("Grey 400", 0xBDBDBD),
    GREY_500("Grey 500", 0x9E9E9E),
    GREY_600("Grey 600", 0x757575),
    GREY_700("Grey 700", 0x616161),
    GREY_800("Grey 800", 0x424242),
    GREY_900("Grey 900", 0x212121),
    GREY_1000("Black 1000", 0x000000),
    GREY_1100("White 1000", 0xffffff);

    override fun getColor(index: Int): Int = values()[index].colorInt
}

enum class BlueGreyPalette(val colorName: String, private val colorInt: Int)
    : MaterialColorPalette {
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

    override fun getColor(index: Int): Int = values()[index].colorInt
}

val materialPalettes = mutableMapOf<String, Array<*>>(
    "Red" to RedPalette.values(),
    "Pink" to PinkPalette.values(),
    "Purple" to PurplePalette.values(),
    "Deep Purple" to DeepPurplePalette.values(),
    "Indigo" to IndigoPalette.values(),
    "Blue" to BluePalette.values(),
    "Light Blue" to LightBluePalette.values(),
    "Cyan" to CyanPalette.values(),
    "Teal" to TealPalette.values(),
    "Green" to GreenPalette.values(),
    "Light Green" to LightGreenPalette.values(),
    "Lime" to LimePalette.values(),
    "Yellow" to YellowPalette.values(),
    "Amber" to AmberPalette.values(),
    "Orange" to OrangePalette.values(),
    "Deep Orange" to DeepOrangePalette.values(),
    "Brown" to BrownPalette.values(),
    "Grey" to GreyPalette.values(),
    "Blue Grey" to BlueGreyPalette.values()
)

