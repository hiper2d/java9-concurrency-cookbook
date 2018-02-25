package com.hiper2d.chapter6.streams

import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.toList

fun main(args: Array<String>) {
    val persons = generatePersonList(10)
    val doubles = ThreadLocalRandom.current().doubles(10, 0.0, 100.0).toList()

    persons.parallelStream().forEach { println("${it.firstName}, ${it.lastName}") }
    println()

    println("Unordered doubles:")
    // sorted() below is not needed but I kept it here to show that normal forEach is not ordered
    doubles.parallelStream().sorted().forEach { println(it) }
    println()

    println("Ordered doubles:")
    doubles.parallelStream().sorted().forEachOrdered { println(it) }
    println()

    println("Ordered persons:")
    persons.parallelStream().sorted().forEachOrdered { println(it) }
    println()

    doubles.parallelStream()
            .peek { println("Step 1: Number: $it") }
            .peek { println("Step 2: Number: $it") }
            .forEach { println("Final Step: Number: $it") }
}