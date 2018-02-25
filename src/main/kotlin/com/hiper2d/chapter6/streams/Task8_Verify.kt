package com.hiper2d.chapter6.streams

fun main(args: Array<String>) {
    val persons = generatePersonList(10)

    val min = persons.parallelStream().map { it.salary }.min(Int::compareTo).get()
    val max = persons.parallelStream().map { it.salary }.max(Int::compareTo).get()
    println("Salaries are between $min and $max")
    println()

    println("Q: Doest everybody has a salary? A: ${persons.parallelStream().allMatch { it.salary > 0 }}")
    println("Q: Does anybody has a salary > 50 000? A: ${persons.parallelStream().anyMatch { it.salary > 50_000 }}")
    println("Q: Nobody has a salary > 100 000, doesn't it? A: ${persons.parallelStream().noneMatch { it.salary > 100_000 }}")
    println()

    val randomFirst = persons.parallelStream().findFirst().get()
    val first = persons.parallelStream().sorted { p1, p2 -> p1.salary - p2.salary }.findFirst().get()
    println("Random first person: $randomFirst")
    println("First person: $first")
}