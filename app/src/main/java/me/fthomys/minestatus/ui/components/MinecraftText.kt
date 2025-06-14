package me.fthomys.minestatus.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A composable that renders Minecraft-formatted text with colors.
 * 
 * Minecraft uses § (section sign) followed by a color code to format text.
 * This composable parses those codes and renders the text with appropriate styling.
 */
@Composable
fun MinecraftText(
    text: String?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fallbackColor: Color = Color.White
) {
    if (text == null) return

    val annotatedString = parseMinecraftText(text, fallbackColor)

    Text(
        text = annotatedString,
        modifier = modifier,
        fontSize = fontSize
    )
}

/**
 * A composable that renders multiple lines of Minecraft-formatted text.
 */
@Composable
fun MinecraftMultilineText(
    lines: List<String>?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fallbackColor: Color = Color.White
) {
    if (lines.isNullOrEmpty()) return

    Column(modifier = modifier) {
        lines.forEach { line ->
            MinecraftText(
                text = line,
                modifier = Modifier.fillMaxWidth(),
                fontSize = fontSize,
                fallbackColor = fallbackColor
            )
        }
    }
}

/**
 * Parses Minecraft formatted text and converts it to an AnnotatedString with appropriate styling.
 */
private fun parseMinecraftText(text: String, fallbackColor: Color): AnnotatedString {
    return buildAnnotatedString {
        var currentColor = fallbackColor
        var bold = false
        var italic = false
        var underline = false
        var strikethrough = false

        var i = 0
        while (i < text.length) {
            if (text[i] == '§' && i + 1 < text.length) {
                val code = text[i + 1].lowercaseChar()
                when (code) {
                    '0' -> currentColor = Color(0xFF000000) // Black
                    '1' -> currentColor = Color(0xFF0000AA) // Dark Blue
                    '2' -> currentColor = Color(0xFF00AA00) // Dark Green
                    '3' -> currentColor = Color(0xFF00AAAA) // Dark Aqua
                    '4' -> currentColor = Color(0xFFAA0000) // Dark Red
                    '5' -> currentColor = Color(0xFFAA00AA) // Dark Purple
                    '6' -> currentColor = Color(0xFFFFAA00) // Gold
                    '7' -> currentColor = Color(0xFFAAAAAA) // Gray
                    '8' -> currentColor = Color(0xFF555555) // Dark Gray
                    '9' -> currentColor = Color(0xFF5555FF) // Blue
                    'a' -> currentColor = Color(0xFF55FF55) // Green
                    'b' -> currentColor = Color(0xFF55FFFF) // Aqua
                    'c' -> currentColor = Color(0xFFFF5555) // Red
                    'd' -> currentColor = Color(0xFFFF55FF) // Light Purple
                    'e' -> currentColor = Color(0xFFFFFF55) // Yellow
                    'f' -> currentColor = Color(0xFFFFFFFF) // White
                    'l' -> bold = true
                    'm' -> strikethrough = true
                    'n' -> underline = true
                    'o' -> italic = true
                    'r' -> {
                        currentColor = fallbackColor
                        bold = false
                        italic = false
                        underline = false
                        strikethrough = false
                    }
                }
                i += 2 // Skip the code
                continue
            }
            withStyle(
                SpanStyle(
                    color = currentColor,
                    fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = when {
                        underline && strikethrough -> TextDecoration.combine(
                            listOf(TextDecoration.Underline, TextDecoration.LineThrough)
                        )
                        underline -> TextDecoration.Underline
                        strikethrough -> TextDecoration.LineThrough
                        else -> TextDecoration.None
                    }
                )
            ) {
                append(text[i].toString())
            }
            i++
        }
    }
}

/**
 * A composable that renders HTML-formatted Minecraft text.
 * This is useful when the API provides HTML-formatted MOTD.
 */
@Composable
fun MinecraftHtmlText(
    html: String?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fallbackColor: Color = Color.White
) {
    if (html == null) return
    val minecraftText = parseHtmlToMinecraftCodes(html)

    MinecraftText(
        text = minecraftText,
        modifier = modifier,
        fontSize = fontSize,
        fallbackColor = fallbackColor
    )
}

/**
 * Parses HTML formatted text and converts it to Minecraft formatting codes.
 * Handles color spans and other HTML elements.
 */
private fun parseHtmlToMinecraftCodes(html: String): String {
    var result = html
    result = result
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&apos;", "'")
        .replace("&nbsp;", " ")
    val spanColorPattern = "<span style=\"color: #([0-9A-Fa-f]{6})\">".toRegex()
    val colorMap = mapOf(
        "000000" to "§0", // Black
        "0000AA" to "§1", // Dark Blue
        "00AA00" to "§2", // Dark Green
        "00AAAA" to "§3", // Dark Aqua
        "AA0000" to "§4", // Dark Red
        "AA00AA" to "§5", // Dark Purple
        "FFAA00" to "§6", // Gold
        "AAAAAA" to "§7", // Gray
        "555555" to "§8", // Dark Gray
        "5555FF" to "§9", // Blue
        "55FF55" to "§a", // Green
        "55FFFF" to "§b", // Aqua
        "FF5555" to "§c", // Red
        "FF55FF" to "§d", // Light Purple
        "FFFF55" to "§e", // Yellow
        "FFFFFF" to "§f"  // White
    )
    result = spanColorPattern.replace(result) { matchResult ->
        val colorCode = matchResult.groupValues[1].uppercase()
        colorMap[colorCode] ?: findClosestColorCode(colorCode)
    }
    result = result.replace("</span>", "§r")
    result = result.replace("<[^>]*>".toRegex(), "")

    return result
}

/**
 * Data class to represent a Minecraft color with its code and RGB values.
 */
private data class MinecraftColor(
    val code: String,
    val r: Int,
    val g: Int,
    val b: Int
)

/**
 * Finds the closest Minecraft color code for a given hex color.
 * This is useful for colors that don't exactly match the standard Minecraft colors.
 */
private fun findClosestColorCode(hexColor: String): String {
    val r = hexColor.substring(0, 2).toInt(16)
    val g = hexColor.substring(2, 4).toInt(16)
    val b = hexColor.substring(4, 6).toInt(16)
    val colors = listOf(
        MinecraftColor("§0", 0, 0, 0),       // Black
        MinecraftColor("§1", 0, 0, 170),     // Dark Blue
        MinecraftColor("§2", 0, 170, 0),     // Dark Green
        MinecraftColor("§3", 0, 170, 170),   // Dark Aqua
        MinecraftColor("§4", 170, 0, 0),     // Dark Red
        MinecraftColor("§5", 170, 0, 170),   // Dark Purple
        MinecraftColor("§6", 255, 170, 0),   // Gold
        MinecraftColor("§7", 170, 170, 170), // Gray
        MinecraftColor("§8", 85, 85, 85),    // Dark Gray
        MinecraftColor("§9", 85, 85, 255),   // Blue
        MinecraftColor("§a", 85, 255, 85),   // Green
        MinecraftColor("§b", 85, 255, 255),  // Aqua
        MinecraftColor("§c", 255, 85, 85),   // Red
        MinecraftColor("§d", 255, 85, 255),  // Light Purple
        MinecraftColor("§e", 255, 255, 85),  // Yellow
        MinecraftColor("§f", 255, 255, 255)  // White
    )
    var closestColor = colors[0]
    var minDistance = Double.MAX_VALUE

    for (color in colors) {
        val distance = sqrt(
            (r - color.r).toDouble().pow(2.0) +
                    (g - color.g).toDouble().pow(2.0) +
                    (b - color.b).toDouble().pow(2.0)
        )

        if (distance < minDistance) {
            minDistance = distance
            closestColor = color
        }
    }

    return closestColor.code
}
