package dev.kdrag0n.colorkt.rgb

import kotlin.math.roundToInt

/**
 * A color in the standard sRGB color space.
 * This is the most common device color space, usually used for final output colors.
 *
 * @see <a href="https://en.wikipedia.org/wiki/SRGB">Wikipedia</a>
 */
public data class Srgb(
    override val r: Double,
    override val g: Double,
    override val b: Double,
) : Rgb {
    // Convenient constructors for quantized values

    /**
     * Constructor for 8-bit integer sRGB components.
     */
    public constructor(
        r: Int,
        g: Int,
        b: Int,
    ) : this(
        r.toDouble() / 255.0,
        g.toDouble() / 255.0,
        b.toDouble() / 255.0,
    )

    /**
     * Constructor for 8-bit packed integer sRGB colors, such as hex color codes.
     */
    public constructor(color: Int) : this(
        r = (color shr 16) and 0xff,
        g = (color shr 8) and 0xff,
        b = color and 0xff,
    )

    /**
     * Convert this color to an 8-bit packed RGB integer (32 bits total)
     *
     * This is equivalent to the integer value of hex color codes (e.g. #FA00FA).
     *
     * @return color as 32-bit integer in RGB8 format
     */
    public fun toRgb8(): Int {
        return (quantize8(r) shl 16) or (quantize8(g) shl 8) or quantize8(b)
    }

    /**
     * Convert this color to an 8-bit hex color code (e.g. #FA00FA).
     *
     * @return color as RGB8 hex code
     */
    public fun toHex(): String {
        return "#" + toRgb8().toString(16).padStart(6, padChar = '0')
    }

    /**
     * Check whether this color is within the sRGB gamut.
     * @return true if color is in gamut, false otherwise
     */
    public fun isInGamut(): Boolean = !r.isNaN() && !g.isNaN() && !b.isNaN() &&
            r in 0.0..1.0 && g in 0.0..1.0 && b in 0.0..1.0

    internal companion object {
        internal fun register() { }

        // Clamp out-of-bounds values
        private fun quantize8(n: Double) = (n * 255.0).roundToInt() and 0xff
    }
}
