package com.hiper2d.character6.streams

import com.hiper2d.randomLocalDate
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier
import java.util.stream.Stream

private val NAMES = arrayOf("Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "James", "John", "Robert", "Michael", "William")
private val LASTNAMES = arrayOf("Smith", "Jones", "Taylor", "Williams", "Brown", "Davies", "Evans", "Wilson", "Thomas", "Roberts")

private class Person(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate,
        val salary: Int,
        val coeficient: Double
): Comparable<Person> {
    override fun compareTo(other: Person): Int {
        val lastNameCompare = lastName.compareTo(other.lastName)
        return when (lastNameCompare) {
            0 -> firstName.compareTo(other.firstName)
            else -> lastNameCompare
        }
    }
}

private class StringSupplier: Supplier<String> {
    private val counter = AtomicInteger(0)

    override fun get(): String = "String ${counter.getAndIncrement()}"
}

private fun generatePersonList(size: Int): List<Person> {
    val rand = Random()
    return (1 until size).map { generatePerson(it, rand) }.toList()
}

private fun generatePerson(id: Int, rand: Random): Person {
    return Person(
            id,
            NAMES[rand.nextInt(10)],
            LASTNAMES[rand.nextInt(10)],
            randomLocalDate(),
            rand.nextInt(100_000),
            rand.nextDouble()
    )
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