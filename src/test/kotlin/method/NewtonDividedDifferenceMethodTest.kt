package method

import io.github.zerumi.method.NewtonDividedDifferenceMethod
import io.github.zerumi.model.Point
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class NewtonDividedDifferenceMethodTest {

    @Test
    fun interpolate() {
        val values: Array<Point> = arrayOf(
            Point(0.15, 1.25),
            Point(0.2, 2.38),
            Point(0.33, 3.79),
            Point(0.47, 5.44),
        )

        val actual = NewtonDividedDifferenceMethod(values).interpolate(0.22)

        assertEquals(2.707, actual, 0.001)
    }
}