package com.hiper2d.character6.streams

import com.hiper2d.randomLocalDate
import java.time.LocalDate
import java.util.*

private val NAMES = arrayOf("Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "James", "John", "Robert", "Michael", "William")
private val LASTNAMES = arrayOf("Smith", "Jones", "Taylor", "Williams", "Brown", "Davies", "Evans", "Wilson", "Thomas", "Roberts")

private val PERSON_COMPARATOR = Comparator.comparing(Person::firstName).thenComparing(Person::lastName)

class Person(
        private val id: Int,
        val firstName: String,
        val lastName: String,
        val birthDate: LocalDate,
        val salary: Int,
        private val coeficient: Double
): Comparable<Person> {
    override fun compareTo(other: Person) = PERSON_COMPARATOR.compare(this, other)

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Person -> this.compareTo(other) == 0
            else -> false
        }
    }

    override fun hashCode(): Int {
        return "$firstName$lastName".hashCode()
    }

    override fun toString(): String = "$firstName $lastName"
}

fun generatePersonList(size: Int): List<Person> {
    val rand = Random()
    return (1 until size).map { generatePerson(it, rand) }.toList()
}

fun generatePerson(id: Int, rand: Random): Person {
    return Person(
            id,
            NAMES[rand.nextInt(10)],
            LASTNAMES[rand.nextInt(10)],
            randomLocalDate(),
            rand.nextInt(100_000),
            rand.nextDouble()
    )
}