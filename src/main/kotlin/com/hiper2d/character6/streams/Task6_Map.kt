package com.hiper2d.character6.streams

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentMap
import java.util.stream.Collectors.counting
import java.util.stream.Collectors.groupingByConcurrent
import kotlin.streams.toList

private class BasicPerson(val name: String, val age: Long)

fun main(args: Array<String>) {
    val persons = generatePersonList(10)

    // Note that if you place the peek after the distinct in parallel stream it won't print anything.
    // Don't clearly understand why.
    val distinctSalariesCount = persons.parallelStream()
            .mapToInt { it.salary }
            .peek { println("Salary: $it") }
            .distinct()
            .peek { println("Hello") } // Doesn't print anything, todo: find out why
            .count()
    println("Distinct salaries count: $distinctSalariesCount")
    println()

    val basicPersons = persons.parallelStream().map { BasicPerson(it.firstName, calculateAge(it.birthDate)) }.toList()
    basicPersons.forEach { println("${it.name} is ${it.age} old") }
    println()

    val wordCount: ConcurrentMap<String, Long> = generateFile(100).parallelStream()
            .flatMap { it.split(" ", ",", ".").stream() }
            .filter { it.isNotEmpty() }
            .sorted()
            .collect(groupingByConcurrent({e: String -> e}, counting()))
    wordCount.forEach { (k, v) -> println("$k: $v") }
    println()
}

private fun calculateAge(birthDate: LocalDate): Long {
    return ChronoUnit.YEARS.between(birthDate, LocalDate.now())
}