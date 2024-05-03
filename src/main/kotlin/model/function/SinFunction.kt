package io.github.zerumi.model.function

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import kotlin.math.sin

object SinFunction : OptionGroup(), Function {
    override fun calculate(x: Double): Double = sin(x)
}
