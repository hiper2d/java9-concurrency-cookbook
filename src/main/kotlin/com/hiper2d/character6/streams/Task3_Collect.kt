package com.hiper2d.character6.streams

import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors.*

fun main(args: Array<String>) {
    val persons = generatePersonList(100)

    val personsByName = persons.parallelStream().collect(groupingByConcurrent(Person::firstName))
    personsByName.forEach { name, personList -> println("$name: There are ${personList.size} persons with this name") }
    println()

    val message = persons.parallelStream().map(Person::toString).collect(joining(", "))
    println("All persons list: $message")
    println()

    val personsBySalary = persons.parallelStream().collect(partitioningBy { it.salary > 50_000 })
    personsBySalary.forEach { k, v -> println("There are ${v.size} persons with (salary > 50 000 = $k)") }
    println()

    val nameMap = persons.parallelStream().collect(
            toConcurrentMap<Person, String, String>(
                    { it.firstName },
                    { it.lastName },
                    { a, b -> "$a, $b" }
            )
    )
    nameMap.forEach { t, u -> println("$t: $u") }
    println()

    val highSalaryPeople = persons.parallelStream().collect<MutableList<Person>>(
            ::ArrayList,
            ::addRichPersonToList,
            { list1, list2 -> list1.addAll(list2) }
    )
    println("High Salary People: ${highSalaryPeople.size}")
    println()

    val peopleNames = persons.parallelStream().collect<ConcurrentHashMap<String, Int>>(
            ::ConcurrentHashMap,
            ::addCountToMap,
            ::mergeMaps
    )
    peopleNames.forEach { t: String, u: Int -> println("$t: $u") }
}

private fun addRichPersonToList(list: MutableList<Person>, person: Person) {
    if (person.salary > 50_000) {
        list.add(person)
    }
}

private fun addCountToMap(map: ConcurrentHashMap<String, Int>, person: Person) {
    map.putIfAbsent(person.firstName, 1)
    map.computeIfPresent(person.firstName) { _, counter -> counter + 1 }
}

private fun mergeMaps(map1: ConcurrentHashMap<String, Int>, map2: ConcurrentHashMap<String, Int>) {
    map2.forEach(10) { k, v ->
        map1.merge(k, v) { v1, v2 -> v1 + v2 } // the merge result is stored in map1
    }
}