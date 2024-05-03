package io.github.zerumi.method.factory

import io.github.zerumi.method.InterpolationMethod
import io.github.zerumi.method.LagrangeMethod
import io.github.zerumi.method.NewtonDividedDifferenceMethod
import io.github.zerumi.method.NewtonSimilarDifferenceMethod
import io.github.zerumi.model.Point
import kotlin.math.abs

interface MethodFactory {
    fun generateMethod(values: Array<Point>): InterpolationMethod
}

class LagrangeMethodFactory : MethodFactory {
    override fun generateMethod(values: Array<Point>): InterpolationMethod {
        return LagrangeMethod(values)
    }
}

class NewtonMethodFactory : MethodFactory {
    override fun generateMethod(values: Array<Point>): InterpolationMethod {
        var similarDifference = true

        for (i in 1..<values.size) {
            if (abs(values[i - 1].x - values[i].x) > 1e-4) similarDifference = false
        }

        return if (similarDifference) NewtonSimilarDifferenceMethod(values)
        else NewtonDividedDifferenceMethod(values)
    }
}

class NewtonDividedDifferenceMethodFactory : MethodFactory {
    override fun generateMethod(values: Array<Point>): InterpolationMethod {
        return NewtonDividedDifferenceMethod(values)
    }
}

class NewtonSimilarDifferenceMethodFactory : MethodFactory {
    override fun generateMethod(values: Array<Point>): InterpolationMethod {
        return NewtonSimilarDifferenceMethod(values)
    }
}
