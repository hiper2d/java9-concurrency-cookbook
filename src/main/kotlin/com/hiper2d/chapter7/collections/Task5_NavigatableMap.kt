package com.hiper2d.chapter7.collections

import java.util.concurrent.ConcurrentNavigableMap
import java.util.concurrent.ConcurrentSkipListMap

private class Contact(val name: String, val phone: String)

private class ContactTask(val id: Char, val map: ConcurrentNavigableMap<String, Contact>): Runnable {

    override fun run() {
        (0..999).forEach {
            val contact = Contact(id.toString(), (it + 1000).toString())
            map[id + contact.phone] = contact
        }
    }
}

fun main(args: Array<String>) {
    val map = ConcurrentSkipListMap<String, Contact>()
    val threads = ('A'..'Z').map {
        val task = ContactTask(it, map)
        val thr = Thread(task)
        thr.start()
        thr
    }
    threads.forEach { it.join() }

    println("Main: Size of the map: ${map.size}")
    val firstEntry = map.firstEntry()
    println("Main: First Entry (id = ${firstEntry.key}): ${firstEntry.value.name}: ${firstEntry.value.phone}")
    val lastEntry = map.lastEntry()
    println("Main: Last Entry (id = ${lastEntry.key}): ${lastEntry.value.name}: ${lastEntry.value.phone}")

    println("Main: Submap from A1996 to B1002")
    val subMap = map.subMap("A1996", "B1002")

    while (subMap.firstEntry() != null) {
        val entry = subMap.pollLastEntry()
        println("${entry.value.name}:${entry.value.phone}")
    }

    println("Main: Submap size ${subMap.size}")
    println("Main: Map size ${map.size}")
}