package com.hiper2d.chapter4.executors

import java.time.LocalDateTime
import java.util.concurrent.*

class DelayedTask(private val name: String): Callable<String> {
    override fun call(): String {
        println("$name: Starting at ${LocalDateTime.now()}")
        return "Hello, world"
    }
}

fun main(args: Array<String>) {
    println("Main: Starting at ${LocalDateTime.now()}")
    val executor = Executors.newScheduledThreadPool(1) as ScheduledThreadPoolExecutor
    (1..5).forEach { executor.schedule(DelayedTask("Task $it"), (it + 1).toLong(), TimeUnit.SECONDS) }
    executor.shutdown()

    executor.awaitTermination(1, TimeUnit.DAYS)
    println("Main: Ends at: ${LocalDateTime.now()}")
}