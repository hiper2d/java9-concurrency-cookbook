package com.hiper2d.character6.streams

import java.util.TreeSet
import kotlin.streams.toList

fun main(args: Array<String>) {
    val persons = generatePersonList(10)
    persons.parallelStream().sorted().forEachOrdered { println(it) }
    println()

    val personsTree = TreeSet(persons)
    (1..10).forEach {
        println(personsTree.stream().parallel().limit(1).toList()[0])
        println(personsTree.stream().unordered().parallel().limit(1).toList()[0])
        println("*********")
    }
}