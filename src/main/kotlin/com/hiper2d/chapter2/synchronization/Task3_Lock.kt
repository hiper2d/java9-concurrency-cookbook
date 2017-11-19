package com.hiper2d.chapter2.synchronization

import java.lang.Thread.sleep
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class PrintQueue(fairMode: Boolean) {
    private val queueLock: Lock = ReentrantLock(fairMode)

    fun printJob(job: Any) {
        printWithLock()
        printWithLock()
    }

    private fun printWithLock() {
        queueLock.lock()
        try {
            val duration = (ThreadLocalRandom.current().nextDouble() * 10_000).toLong()
            println("${Thread.currentThread().name}: PrintQueue: Printing a Job during ${duration / 1000} seconds")
            sleep(duration)
        } finally {
            queueLock.unlock()
        }
    }
}

class Job(private var printQueue: PrintQueue): Runnable {
    override fun run() {
        println("${Thread.currentThread().name}: Going to print job")
        printQueue.printJob(Object())
        println("${Thread.currentThread().name}: The document has been printed")
    }
}

fun main(args: Array<String>) {
    testPrintQueue(false)
    testPrintQueue(true)
}

private fun testPrintQueue(mode: Boolean) {
    println("Running example with fair-mode = $mode")
    val printQueue = PrintQueue(mode)

    val threads = arrayOfThreads(printQueue)

    threads.forEach { it.join() }
}

private fun arrayOfThreads(printQueue: PrintQueue): Array<Thread> {
    return Array(10) {
        val tr = Thread(Job(printQueue), "Thread $it")
        tr.start()
        tr
    }
}