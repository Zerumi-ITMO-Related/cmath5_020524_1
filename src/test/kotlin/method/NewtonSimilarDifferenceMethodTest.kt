package method

import io.github.zerumi.method.NewtonSimilarDifferenceMethod
import io.github.zerumi.model.Point
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class NewtonSimilarDifferenceMethodTest {

    @Test
    fun interpolateTest1() {
        val values: Array<Point> = arrayOf(
            Point(0.1, 1.25),
            Point(0.2, 2.38),
            Point(0.3, 3.79),
            Point(0.4, 5.44),
            Point(0.5, 7.14),
        )

        val actual = NewtonSimilarDifferenceMethod(values).interpolate(0.15)

        assertEquals(1.78336, actual, 0.00001)
    }

    @Test
    fun interpolateTest2() {
        val values: Array<Point> = arrayOf(
            Point(0.1, 1.25),
            Point(0.2, 2.38),
            Point(0.3, 3.79),
            Point(0.4, 5.44),
            Point(0.5, 7.14),
        )

        val actual = NewtonSimilarDifferenceMethod(values).interpolate(0.22)

        assertEquals(2.63368, actual, 0.00001)
    }

    @Test
    fun interpolateTest3() {
        val values: Array<Point> = arrayOf(
            Point(0.1, 1.25),
            Point(0.2, 2.38),
            Point(0.3, 3.79),
            Point(0.4, 5.44),
            Point(0.5, 7.14),
        )

        val actual = NewtonSimilarDifferenceMethod(values).interpolate(0.47)

        assertEquals(6.64208, actual, 0.00001)
    }
}