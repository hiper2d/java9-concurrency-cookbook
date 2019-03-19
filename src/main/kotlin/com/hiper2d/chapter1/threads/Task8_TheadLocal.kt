package com.hiper2d.chapter1.threads

import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit


class UnsafeTask: Runnable {
    private lateinit var date: LocalDateTime

    override fun run() {
        date = LocalDateTime.now()
        println("Starting Thread: $${Thread.currentThread().id}: $date")
        TimeUnit.SECONDS.sleep(Math.rint(ThreadLocalRandom.current().nextDouble() * 10).toLong())
        println("Ending Thread: $${Thread.currentThread().id}: $date")
    }
}

class SafeTask: Runnable {
    private var date = ThreadLocal.withInitial { LocalDateTime.now() }

    override fun run() {
        println("Starting Thread: $${Thread.currentThread().id}: ${date.get()}")
        TimeUnit.SECONDS.sleep(Math.rint(ThreadLocalRandom.current().nextDouble() * 10).toLong())
        println("Ending Thread: $${Thread.currentThread().id}: ${date.get()}")
    }
}

fun main() {
    // val task = UnsafeTask()
    val task = SafeTask()
    repeat(3) {
        Thread(task).start()
        TimeUnit.SECONDS.sleep(2)
    }
}