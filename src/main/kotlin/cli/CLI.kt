package io.github.zerumi.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import io.github.zerumi.method.LagrangeMethod
import io.github.zerumi.method.NewtonSimilarDifferenceMethod
import io.github.zerumi.method.factory.LagrangeMethodFactory
import io.github.zerumi.method.factory.NewtonDividedDifferenceMethodFactory
import io.github.zerumi.method.factory.NewtonMethodFactory
import io.github.zerumi.method.factory.NewtonSimilarDifferenceMethodFactory
import io.github.zerumi.model.Point
import io.github.zerumi.model.function.SinFunction
import io.github.zerumi.model.function.SquareRootFunction
import java.nio.charset.Charset
import kotlin.system.exitProcess

sealed class LoadConfig(name: String) : OptionGroup(name)

class FromFile : LoadConfig("Options for loading from disk") {
    val path by option(
        "-f", "--file",
        help = "Filepath"
    ).file().required()
}

class FromCLI : LoadConfig("Options for loading from CLI") {
    val count by option(
        "-c", "--count",
        help = "Number of elements"
    ).int().prompt("Enter amount of elements")
}

class FromFunction : LoadConfig("Options for loading from network") {
    val function by option(
        "-fx", "--function",
        help = "Function to calculate"
    ).choice(
        "sin" to SinFunction,
        "sqrt" to SquareRootFunction
    ).required()

    val count by option(
        "-ac", "--arg-count",
        help = "Number of elements"
    ).int().prompt("Enter amount of elements")
}

class CLI : CliktCommand() {
    private val infiniteMode by option().boolean().default(false)

    private val method by option(
        "-m",
        "--method"
    ).choice(
        "lagrange" to LagrangeMethodFactory(),
        "newtonDividedDiff" to NewtonDividedDifferenceMethodFactory(),
        "newtonSimilarDiff" to NewtonSimilarDifferenceMethodFactory(),
    )

    private val choice by option(
        "--from", help = "Whether to choose from"
    ).groupChoice(
        "file" to FromFile(),
        "cli" to FromCLI(),
        "function" to FromFunction(),
    ).required()

    private val graphPointsCount by option(
        "--graph-points-count", help = "Number of graph plot points"
    ).int().default(25)

    override fun run() {
        val rawKnownPoints : Array<Point>
        try {
            rawKnownPoints = when (val it = choice) {
                is FromFile -> {
                    parseFile(it)
                }

                is FromCLI -> {
                    useFullCli(it)
                }

                is FromFunction -> {
                    useCLIWithFunction(it)
                }
            }
        } catch (e: Exception) {
            println("Found invalid input! Input should be stream of decimal numbers with dot as separator")
            println("Please, try again")
            exitProcess(2)
        }

        if (rawKnownPoints.map { it.x }.toSet().size != rawKnownPoints.size) {
            println("Points shouldn't contain equal X arguments!")
            println("Found ${rawKnownPoints.size} points " +
                    "with only ${rawKnownPoints.map { it.x }.toSet().size} unique X argument!")
            exitProcess(1)
        }

        val knownPoints = rawKnownPoints.sortedBy { it.x }.toTypedArray()

        val newtonMethod = NewtonMethodFactory().generateMethod(knownPoints)

        if (newtonMethod is NewtonSimilarDifferenceMethod) {
            println("Found similar differences. Here's the matrix:")
            val list = mutableListOf<MutableList<Double>>()
            for (i in knownPoints.indices) {
                list.add(mutableListOf())
            }
            for ((k, v) in newtonMethod.diffTable) {
                list[k.first].add(v)
            }
            drawMatrix(list.map { it.toTypedArray() }.toTypedArray())
        }
        try {
            if (method != null) {
                val factoredMethod = method!!.generateMethod(knownPoints)
                do {
                    print("Enter point X: ")
                    val currentX = readln().toDouble()
                    println("Solution: ${factoredMethod.interpolate(currentX)}")
                } while (infiniteMode)
            } else {
                print("Enter point X: ")
                val currentX = readln().toDouble()

                println("Solution by Lagrange method: ${LagrangeMethod(knownPoints).interpolate(currentX)}")
                println(
                    "Solution by Newton method: ${
                        newtonMethod.interpolate(currentX)
                    }"
                )
            }
        } catch (e: Exception) {
            println("Invalid input! Use dot as decimal separator. Example of correct number: 42.42")
        }

        println("Graph plot for Newton interpolation:")
        drawPlot(
            newtonMethod,
            graphPointsCount,
            knownPoints.first().x,
            knownPoints.last().x,
        )
    }

    private fun parseFile(options: FromFile): Array<Point> {
        val values = mutableListOf<Point>()

        val numbers = options.path
            .readText(Charset.defaultCharset())
            .split(' ', '\n').map(String::toDouble)

        for (i in numbers.indices step 2) {
            values.add(Point(numbers[i], numbers[i + 1]))
        }

        return values.toTypedArray()
    }

    private fun useFullCli(options: FromCLI): Array<Point> {
        val values = mutableListOf<Point>()

        println("Please, enter X/Y coordinates:")
        for (i in 0..<options.count) {
            print("X${i}: ")
            val currentX = readln().toDouble()
            print("Y${i}: ")
            val currentY = readln().toDouble()
            values.add(Point(currentX, currentY))
        }

        return values.toTypedArray()
    }

    private fun useCLIWithFunction(options: FromFunction): Array<Point> {
        val values = mutableListOf<Point>()

        println("Please, enter X coordinates:")
        for (i in 0..<options.count) {
            print("${i}: ")
            val currentX = readln().toDouble()
            values.add(Point(currentX, options.function.calculate(currentX)))
        }

        return values.toTypedArray()
    }
}
