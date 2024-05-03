package io.github.zerumi.cli

import com.indvd00m.ascii.render.Region
import com.indvd00m.ascii.render.Render
import com.indvd00m.ascii.render.api.IRender
import com.indvd00m.ascii.render.elements.Rectangle
import com.indvd00m.ascii.render.elements.plot.Axis
import com.indvd00m.ascii.render.elements.plot.AxisLabels
import com.indvd00m.ascii.render.elements.plot.Plot
import com.indvd00m.ascii.render.elements.plot.api.IPlotPoint
import com.indvd00m.ascii.render.elements.plot.misc.PlotPoint
import io.github.zerumi.method.InterpolationMethod

fun drawPlot(
    interpolationMethod: InterpolationMethod,
    pointsCount: Int,
    firstX: Double,
    lastX: Double
) {
    val points: MutableList<IPlotPoint> = ArrayList()
    val xSize = lastX - firstX
    val startX = firstX - 0.1 * xSize
    val finishX = lastX + 0.1 * xSize
    val step = (finishX - startX) / pointsCount
    for (i in 0..pointsCount) {
        val currentX = startX + i * step
        val decimalPoint = interpolationMethod.interpolate(currentX)
        val plotPoint: IPlotPoint = PlotPoint(currentX, decimalPoint)
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
