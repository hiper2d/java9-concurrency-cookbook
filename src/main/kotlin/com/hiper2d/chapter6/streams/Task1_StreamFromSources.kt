package com.hiper2d.chapter6.streams

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier
import java.util.stream.Stream

private class StringSupplier: Supplier<String> {
    private val counter = AtomicInteger(0)

    override fun get(): String = "String ${counter.getAndIncrement()}"
}

fun main(args: Array<String>) {
    println("From a Collection:")
    val personList = generatePersonList(10_000)
    val personStream = personList.parallelStream()
    println("Number of persons: ${personStream.count()}")
    println()

    println("From a Supplier:")
    val supplier = StringSupplier()
    val streamGenerator = Stream.generate(supplier)
    streamGenerator.parallel().limit(10).forEach { println(it) }
    println()

    println("From a predefined set of elements:")
    val elementStream = Stream.of("Peter", "John", "Mary")
    elementStream.parallel().forEach { println(it) }
    println()

    println("From a File:")
    val path = Paths.get(StringSupplier::class.java.classLoader.getResource("data/nursery.data").toURI())
    val linesStream = Files.lines(path)
    println("Number of lines in the file: ${linesStream.parallel().count()}")
    linesStream.close() // close because it uses io resources
    println()

    println("From a Directory:")
    val directoryStream = Files.list(Paths.get(System.getProperty("user.home")))
    println("Number of elements (files and folders): ${directoryStream.parallel().count()}")
    directoryStream.close() // close because it uses io resources
    println()

    println("From an Array:")
    val arr = Array(10) { it }
    val streamFromArray = Arrays.stream(arr)
    streamFromArray.parallel().forEach { println(it) }
    println()

    println("DoubleStream from an Array of Doubles:")
    val doubleStream = Random().doubles(10)
    val doubleStreamAverage = doubleStream.parallel().peek { println(it) }.average().asDouble
    println("Average double: $doubleStreamAverage ")
    println()

    println("Concatenating streams:")
    val stream1 = Stream.of(1, 2, 3)
    val stream2 = Stream.of(4, 5, 6)
    val concatStream = Stream.concat(stream1, stream2)
    concatStream.parallel().forEach { println(it) }
}