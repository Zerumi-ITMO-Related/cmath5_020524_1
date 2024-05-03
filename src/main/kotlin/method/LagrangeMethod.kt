package io.github.zerumi.method

import io.github.zerumi.model.Point

class LagrangeMethod(
    private val knownValues: Array<Point>
) : InterpolationMethod {
    override fun interpolate(x: Double): Double {
        var sum = 0.0

        for (i in knownValues.indices) {
            var divider = 1.0
            var divisor = 1.0
            for (j in knownValues.indices) {
                if (i == j) continue

                divider *= x - knownValues[j].x
                divisor *= knownValues[i].x - knownValues[j].x
            }
            sum += divider / divisor * knownValues[i].y
        }

        return sum
    }
}
