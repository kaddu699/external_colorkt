package dev.kdrag0n.colorkt.ucs.lab

import dev.kdrag0n.colorkt.tristimulus.CieXyz
import dev.kdrag0n.colorkt.illuminants.Illuminants
import dev.kdrag0n.colorkt.util.cbrt
import kotlin.math.pow

/**
 * A color in the CIE L*a*b* uniform color space, which represents colors in [dev.kdrag0n.colorkt.ucs.lab.Lab] form.
 * This is the most well-known uniform color space, but more modern alternatives such as
 * [dev.kdrag0n.colorkt.ucs.lab.Oklab] tend to be more perceptually uniform.
 *
 * Note that this implementation uses a white point of D65, like sRGB.
 * It does not implement CIELAB D50.
 *
 * @see <a href="https://en.wikipedia.org/wiki/CIELAB_color_space">Wikipedia</a>
 */
class CieLab(
    override val L: Double,
    override val a: Double,
    override val b: Double,
) : Lab {
    override fun toLinearSrgb() = toCieXyz().toLinearSrgb()

    /**
     * Convert this color to the CIE 1931 XYZ color space.
     *
     * @see dev.kdrag0n.colorkt.tristimulus.CieXyz
     * @return Color in CIE 1931 XYZ
     */
    fun toCieXyz(): CieXyz {
        val lp = (L + 16.0) / 116.0

        return CieXyz(
            x = Illuminants.D65.x * fInv(lp + (a / 500.0)),
            y = Illuminants.D65.y * fInv(lp),
            z = Illuminants.D65.z * fInv(lp - (b / 200.0)),
        )
    }

    companion object {
        private fun f(x: Double) = if (x > 216.0/24389.0) {
            cbrt(x)
        } else {
            x / (108.0/841.0) + 4.0/29.0
        }

        private fun fInv(x: Double) = if (x > 6.0/29.0) {
            x.pow(3)
        } else {
            (108.0/841.0) * (x - 4.0/29.0)
        }

        /**
         * Convert this color to the CIE L*a*b* uniform color space.
         *
         * @see dev.kdrag0n.colorkt.ucs.lab.CieLab
         * @return Color in CIE L*a*b* UCS
         */
        fun CieXyz.toCieLab(): CieLab {
            return CieLab(
                L = 116.0 * f(y / Illuminants.D65.y) - 16.0,
                a = 500.0 * (f(x / Illuminants.D65.x) - f(y / Illuminants.D65.y)),
                b = 200.0 * (f(y / Illuminants.D65.y) - f(z / Illuminants.D65.z)),
            )
        }
    }
}