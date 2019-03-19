package com.hiper2d.chapter1.threads

import com.hiper2d.isPrime
import com.hiper2d.logger
import com.hiper2d.printThreadInfo

class Calculator: Runnable {
    override fun run() {
        println("Thread '${Thread.currentThread().name}' stared")
        val primes = (1 until 20_000L).filter { it.isPrime() }.count()
        println("Thread '${Thread.currentThread().name}' ends. Threre are ${primes} primes.")
    }
}

val logger = logger<Calculator>()

fun main() {
    println("Minimum priority: ${Thread.MIN_PRIORITY}")
    println("Normal priority: ${Thread.NORM_PRIORITY}")
    println("Minimum priority: ${Thread.MAX_PRIORITY}")

    val statuses = Array<Thread.State?>(10) { null }
    val threads = arrayOfThreads()

    threads.forEachIndexed { index, thread ->
        logger.debug("Main: Status of thread ${thread.state}")
        statuses[index] = thread.state
        thread.start()
    }

    while (!threads.all { it.state == Thread.State.TERMINATED }) {
        threads.forEachIndexed {i, thread ->
            if (thread.state != statuses[i]) {
                printThreadInfo(logger, thread, statuses[i])
                statuses[i] = thread.state
            }
        }
    }
}

private fun arrayOfThreads(): Array<Thread> {
    return Array(10) {
        val newTread = Thread(Calculator())
        newTread.name = "My Thread $it"
        when (it.rem(2) == 0) {
            true -> newTread.priority = Thread.MAX_PRIORITY
            false -> newTread.priority = Thread.MIN_PRIORITY
        }
        newTread
    }
}