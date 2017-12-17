package com.hiper2d.chapter4.executors

import java.lang.Thread.sleep
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class CancelledTask: Callable<String> {
    override fun call(): String {
        while (true) {
            println("Task: Test")
            sleep(500)
        }
    }
}

fun main(args: Array<String>) {
    println("Main: Started")
    val executor = Executors.newCachedThreadPool() as ThreadPoolExecutor

    println("Main: Executing the Task")
    val result = executor.submit(CancelledTask())
    sleep(2_000)

    println("Main: Canceling the task")
    result.cancel(true)

    println("Main: Canceled: ${result.isCancelled}")
    println("Main: Done: ${result.isDone}")

    executor.shutdown()
    println("Main: The executor has finished")
}
