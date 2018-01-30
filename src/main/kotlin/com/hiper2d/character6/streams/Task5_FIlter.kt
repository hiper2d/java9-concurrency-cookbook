package com.hiper2d.character6.streams

import java.util.*

fun main(args: Array<String>) {
    val persons = generatePersonList(10)
    val ints = arrayOf(1, 2, 3, 3, 3, 4, 5, 6, 7)

    persons.parallelStream().forEach { println(it) }
    println()

    persons.parallelStream().distinct().forEach { println(it) }
    println()

    Arrays.stream(ints).parallel().distinct().forEach { println(it) }
    println()

    println("Guys with the salary lower than 30 000:")
    persons.parallelStream().filter { it.salary < 30_000 }.forEach { println(it) }
    println()

    println("Numbers which are lower than 2:")
    Arrays.stream(ints).parallel().filter { it < 2 }.forEach { println(it) }
    println()

    println("Top 5 the richest persons:")
    persons.parallelStream().map(Person::salary).sorted(reverseOrder()).limit(5).forEachOrdered { println(it) }
    println()

    println("The richest persons except first 5:")
    persons.parallelStream().map(Person::salary).sorted(reverseOrder()).skip(5).forEachOrdered { println(it) }
    println()
}