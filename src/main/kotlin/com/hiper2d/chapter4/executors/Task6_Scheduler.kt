package com.hiper2d.chapter4.executors

import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ScheduledTesk(private val name: String): Runnable {
    override fun run() {
        println("$name: Executed at ${LocalDateTime.now()}")
    }
}

fun main(args: Array<String>) {
    println("Main: Starting at: ${LocalDateTime.now()}")
    val executor = Executors.newScheduledThreadPool(1)

    val scheduledFuture: ScheduledFuture<*> = executor.scheduleAtFixedRate(ScheduledTesk("Task"), 1, 2, TimeUnit.SECONDS)
    (1..10).forEach {
        println("Main: Delay: ${scheduledFuture.getDelay(TimeUnit.MILLISECONDS)}") // Milliseconds left before the next run
        sleep(500)
    }

    executor.shutdown()
    TimeUnit.SECONDS.sleep(5) // Put the thread to sleep for 5 seconds to verify that the periodic tasks have finished
    println("Main: Finished at ${LocalDateTime.now()}")
}