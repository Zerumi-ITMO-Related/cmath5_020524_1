package io.github.zerumi.method

import io.github.zerumi.model.Point
import io.github.zerumi.model.Window

class NewtonDividedDifferenceMethod(
    private val knownValues: Array<Point>
) : InterpolationMethod {

    private val diffTable: MutableMap<Window, Double> = mutableMapOf()

    private val window: Array<Int>

    init {
        var start = 0
        window = generateSequence { start++.takeIf { it < knownValues.size } }.toList().toTypedArray()
        for (i in knownValues.indices) {
            diffTable[Window(arrayOf(i))] = knownValues[i].y
        }
        buildDifferences(2, 0)
    }

    private fun buildDifferences(currentWindowSize: Int, offset: Int) {
        if (currentWindowSize > knownValues.size) return
        if (currentWindowSize + offset > knownValues.size) {
            buildDifferences(currentWindowSize + 1, 0)
            return
        }

        val currentDifference = Window(window.sliceArray(0 + offset until currentWindowSize + offset))

        val firstPreviousDifference =
            Window(window.slice(0 + offset + 1 until currentWindowSize + offset).toTypedArray())
        val secondPreviousDifference =
            Window(window.slice(0 + offset until currentWindowSize + offset - 1).toTypedArray())

        val firstPreviousDifferenceValue = diffTable[firstPreviousDifference]!!
        val secondPreviousDifferenceValue = diffTable[secondPreviousDifference]!!

        diffTable[currentDifference] =
            (firstPreviousDifferenceValue - secondPreviousDifferenceValue) /
                    (knownValues[currentDifference.window.last()].x - knownValues[currentDifference.window.first()].x)

        buildDifferences(currentWindowSize, offset + 1)
    }

    override fun interpolate(x: Double): Double {
        var result = knownValues[0].y

        for (i in 1 until knownValues.size) {
            val window = Window(window.sliceArray(0..i))

            var currentResult = diffTable[window]!!

            for (j in 0 until i) {
                currentResult *= x - knownValues[j].x
            }

            result += currentResult
        }

        return result
    }
}