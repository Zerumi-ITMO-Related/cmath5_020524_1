package io.github.zerumi.method

import io.github.zerumi.cli.drawMatrix
import io.github.zerumi.model.Point
import kotlin.math.pow

class NewtonSimilarDifferenceMethod(
    private val knownValues: Array<Point>
) : InterpolationMethod {

    val diffTable: MutableMap<Pair<Int, Int>, Double> = mutableMapOf()

    init {
        for (i in knownValues.indices) {
            diffTable[Pair(0, i)] = knownValues[i].y
        }

        for (i in 1..<knownValues.size) {
            for (j in 0..<knownValues.size - i) {
                diffTable[Pair(i, j)] = diffTable[Pair(i - 1, j + 1)]!! -
                        diffTable[Pair(i - 1, j)]!!
            }
        }
    }

    private fun factorial(n: Int): Int {
        if (n == 1) return 1
        return n * factorial(n - 1)
    }

    private fun calculateTForward(t: Double, n: Int): Double {
        var result = 1.0

        for (i in 0..<n) {
            result *= (t - i)
        }

        return result / factorial(n)
    }

    private fun interpolateForward(t: Double, n: Int): Double {
        var result = diffTable[Pair(0, n)]!!
        for (i in 1..<knownValues.size - n) {
            result += diffTable[Pair(i, n)]!! * calculateTForward(t, i)
        }
        return result
    }


    private fun calculateTBackward(t: Double, n: Int): Double {
        var result = 1.0

        for (i in 0..<n) {
            result *= (t + i)
        }

        return result / factorial(n)
    }

    private fun interpolateBackward(t: Double, n: Int): Double {
        var result = diffTable[Pair(0, n)]!!
        for (i in 1..n) {
            result += diffTable[Pair(i, n - i)]!! * calculateTBackward(t, i)
        }
        return result
    }

    private fun genericInterpolation(x: Double, h: Double): Double {
        var result = diffTable[Pair(0, 0)]!!

        for (i in 1..<knownValues.size) {
            var element = diffTable[Pair(i, 0)]!!
            for (j in 0..<i) {
                element *= (x - knownValues[j].x)
            }
            element /= h.pow(i)
            element /= factorial(i)

            result += element
        }

        return result
    }

    override fun interpolate(x: Double): Double {
        val leftBorder = knownValues.first().x
        val rightBorder = knownValues.last().x

        val halvedSize = (rightBorder - leftBorder) / 2.0

        val h = knownValues.component2().x - knownValues.component1().x
        var n = ((x - knownValues.first().x + h) / h).toInt() - 1
        if (x > leftBorder + halvedSize) n++

        if (n < 0 || n >= knownValues.size) return genericInterpolation(x, h)

        val t = (x - knownValues[n].x) / h

        return if (x <= leftBorder + halvedSize) interpolateForward(t, n)
        else interpolateBackward(t, n)
    }
}
