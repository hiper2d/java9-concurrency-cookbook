package com.hiper2d.character6.streams

import java.util.concurrent.ThreadLocalRandom

private class Point(val x: Double, val y: Double)

private fun generateDoubleStream() = ThreadLocalRandom.current().doubles(10_000, 0.0, 10_000.0)

private fun generatePointList(size: Int) = (1 until size).map { Point(ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble()) }.toList()

fun main(args: Array<String>) {
    val doubleStream = generateDoubleStream()
    // val doubles = doubleStream.toList()

    val numberOfElements = doubleStream.parallel().count()
    println("The list of numbers has $numberOfElements elements.")

    val sum = generateDoubleStream().parallel().sum()
    println("Its numbers sum is $sum")

    val avg = generateDoubleStream().parallel().average().asDouble
    println("Its numbers have an average value of $avg")

    val max = generateDoubleStream().max().asDouble
    println("The maximum value in the list is $max")

    val min = generateDoubleStream().min().asDouble
    println("The minimum value in the list is $min")
}