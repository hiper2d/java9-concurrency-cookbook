package com.hiper2d.chapter7.collections.task8.atomicarrays

import com.hiper2d.logger
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.atomic.AtomicLongArray

private const val THREADS = 100
private const val VECTOR_SIZE = 1_000_000
private val logger = logger("com.hiper2d.chapter7.collections.task8.atomicarrays")

private class Incrementer(val vector: AtomicLongArray): Runnable {
    override fun run() {
        increment(vector)
    }
}

private class Decrementer(val vector: AtomicLongArray): Runnable {
    override fun run() {
        decrement(vector)
    }
}

private fun increment(vector: AtomicLongArray) {
    (1 until vector.length()).forEach { vector.getAndIncrement(it) }
}

private fun decrement(vector: AtomicLongArray) {
    (1 until vector.length()).forEach { vector.getAndDecrement(it) }
}

private fun validateVector(vector: AtomicLongArray): Int {
    val errorCount = (1 until VECTOR_SIZE).filter { vector.get(it) != 0L }.count()
    logger.debug("Error count: $errorCount")

    if (errorCount == 0) {
        logger.debug("No errors found")
    }
    return errorCount
}

fun runWithThreads(): Int {
    val vector = AtomicLongArray(VECTOR_SIZE)
    val incThreads = Array(100) { Thread(Incrementer(vector)) }
    val decThreads = Array(100) { Thread(Decrementer(vector)) }

    incThreads.forEach { it.start() }
    decThreads.forEach { it.start() }

    incThreads.forEach { it.join() }
    decThreads.forEach { it.join() }

    return validateVector(vector)
}

fun runWithCoroutines(): Int {
    val pool = newFixedThreadPoolContext(THREADS * 2, "Async")
    val vector = AtomicLongArray(VECTOR_SIZE)

    runBlocking {
        val incJobs = (1 until THREADS).map {
            async(pool) { increment(vector) }
        }
        val decJobs = (1 until THREADS).map {
            async(pool) { decrement(vector) }
        }

        incJobs.forEach { it.join() }
        decJobs.forEach { it.join() }
    }

    return validateVector(vector)
}

fun main(args: Array<String>) {
    logger.debug("hey")
    // runWithThreads()
    runWithCoroutines()
}