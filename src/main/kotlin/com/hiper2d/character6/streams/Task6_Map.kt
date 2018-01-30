package com.hiper2d.character6.streams

import java.time.LocalDate

private class BasicPerson(val name: String, val age: Int)

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

    val basicPersons = persons.parallelStream().map { BasicPerson(it.firstName, calculateAge(it.birthDate)) }
}

private fun calculateAge(birthDate: LocalDate): Int {
    val currentYear: Int = LocalDate.now().year
    val birthYear: Int = birthDate.year
    return currentYear - birthYear
}