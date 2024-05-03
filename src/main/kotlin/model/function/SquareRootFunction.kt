package io.github.zerumi.model.function

import kotlin.math.sqrt

object SquareRootFunction : Function {
    override fun calculate(x: Double): Double = sqrt(x)
}
