package com.hiper2d.chapter3.utilities

import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

private class PrintQueue  (
        private val semaphore: Semaphore = Semaphore(3),
        private val freePrinters: Array<Boolean> = Array(3) { true },
        private val lockPrinters: Lock = ReentrantLock()
) {
    private val rand = ThreadLocalRandom.current()

    fun printJob(document: Any) {
        try {
            semaphore.acquire()
            val duration = ThreadLocalRandom.current().nextLong(10)
            val assignedPrinter = getPrinter()
            println("${Date()} - ${Thread.currentThread().name} PrintQueue: Printing a Job in Printer $assignedPrinter during $duration seconds")
            freePrinters[assignedPrinter] = true
        } finally {
            semaphore.release()
        }

    }

    fun getPrinter(): Int {
        val ret: Int
        try {
            lockPrinters.lock()
            ret = freePrinters.indexOfFirst { it }
            if (ret != -1) {
                freePrinters[ret] = false
            }
        } finally {
            lockPrinters.unlock()
        }
        return ret
    }
}

private class PrintJob(private val printQueue: PrintQueue): Runnable {
    override fun run() {
        println("${Thread.currentThread().name}: Going to print a job")
        printQueue.printJob("some string")
        println("${Thread.currentThread().name}: The document has been printed")
    }
}

fun main(args: Array<String>) {
    val printQueue = PrintQueue()
    (1..12).forEach {
        Thread(PrintJob(printQueue), "Thread$it").start()
    }
}