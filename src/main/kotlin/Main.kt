package io.github.zerumi

import com.indvd00m.ascii.render.Region
import com.indvd00m.ascii.render.Render
import com.indvd00m.ascii.render.api.IRender
import com.indvd00m.ascii.render.elements.Rectangle
import com.indvd00m.ascii.render.elements.plot.Axis
import com.indvd00m.ascii.render.elements.plot.AxisLabels
import com.indvd00m.ascii.render.elements.plot.Plot
import com.indvd00m.ascii.render.elements.plot.api.IPlotPoint
import com.indvd00m.ascii.render.elements.plot.misc.PlotPoint
import io.github.zerumi.method.NewtonDividedDifferenceMethod
import io.github.zerumi.model.Point


fun main() {
    val values: Array<Point> = arrayOf(
        Point(0.15, 1.25),
        Point(0.2, 2.38),
        Point(0.33, 3.79),
        Point(0.47, 5.44),
    )

    val newtonDividedDifferenceMethod = NewtonDividedDifferenceMethod(values)

    val points: MutableList<IPlotPoint> = ArrayList()
    for (i in 0..20) {
        val decimalPoint = newtonDividedDifferenceMethod.interpolate(i.toDouble())
        val plotPoint: IPlotPoint = PlotPoint(i.toDouble(), decimalPoint)
        points.add(plotPoint)
    }
    val render: IRender = Render()
    val builder = render.newBuilder()
    builder.width(80).height(20)
    builder.element(Rectangle(0, 0, 80, 20))
    builder.layer(Region(1, 1, 78, 18))
    builder.element(Axis(points, Region(0, 0, 78, 18)))
    builder.element(AxisLabels(points, Region(0, 0, 78, 18)))
    builder.element(Plot(points, Region(0, 0, 78, 18)))
    val canvas = render.render(builder.build())
    val s = canvas.text
    println(s)
}