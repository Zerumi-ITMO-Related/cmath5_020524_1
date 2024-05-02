package method

import io.github.zerumi.method.LagrangeMethod
import io.github.zerumi.model.Point
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class LagrangeMethodTest {

    @Test
    fun interpolateTest1() {
        val values: Array<Point> = arrayOf(
            Point(0.1, 1.25),
            Point(0.2, 2.38),
            Point(0.3, 3.79),
            Point(0.4, 5.44),
            Point(0.5, 7.14),
        )

        val actual = LagrangeMethod(values).interpolate(0.35)

        assertEquals(4.59336, actual, 0.00001)
    }

    @Test
    fun interpolateTest2() {
        val values: Array<Point> = arrayOf(
            Point(100.0, 10.0),
            Point(121.0, 11.0),
            Point(144.0, 12.0),
        )

        val actual = LagrangeMethod(values).interpolate(105.00)

        assertEquals(10.245624, actual, 0.000001)
    }
}