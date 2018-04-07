package com.hiper2d.chapter7.collections

import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ThreadLocalRandom

class Operation(val user: String, val operation: String, val date: LocalDateTime)

class HashFilter(val userHash: ConcurrentMap<String, Deque<Operation>>): Runnable {

    override fun run() {
        val rand = ThreadLocalRandom.current()
        (1..100).forEach {
            val op = generateOperation(rand)
            putOperationInHash(userHash, op)
        }
    }

    private fun generateOperation(rand: ThreadLocalRandom): Operation {
        val op = Operation(
                "USER${rand.nextInt(100)}",
                "OPERATION${rand.nextInt(100)}",
                LocalDateTime.now()
        )
        return op
    }

    private fun putOperationInHash(userHash: ConcurrentMap<String, Deque<Operation>>, operation: Operation) {
        val deque = userHash.computeIfAbsent(operation.user) {
            ConcurrentLinkedDeque<Operation>()
        }
        deque.add(operation)
    }
}

fun main(args: Array<String>) {
    val userHash = ConcurrentHashMap<String, Deque<Operation>>()
    val hashFilter = HashFilter(userHash)

    val threads = startTenHashFilterThreads(hashFilter)
    threads.forEach { it.join() }

    println("User hash size is ${userHash.size}")

    userHash.forEachEntry(10) {  entry -> println("${Thread.currentThread().name}: ${entry.key}") }
    println()

    println("Search first operation ends with 1:")
    val op = userHash.search(10) { _, list ->
        list.find { it.operation.endsWith("1") }
    }
    println("The operation we have found is ${op?.user} ${op?.operation} ${op?.date}")
    println()

    println("Find users with more than 10 operations:")
    val searched = userHash.filter { it.value.size > 10 }.map { it.key }.sorted()
    println("The user we have found is $searched")
    println()

    val total = userHash.reduceValuesToInt(10, { it.size }, 0) { i, j -> i + j }
    println("Total number of operations in hash: $total")
}

private fun startTenHashFilterThreads(hashFilter: HashFilter) =
    (1..10).map {
        val thrd = Thread(hashFilter)
        thrd.start()
        thrd
}