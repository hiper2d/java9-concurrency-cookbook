package com.hiper2d.chapter4.executors

import java.lang.Thread.sleep
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

class CancelledTask: Callable<String> {
    override fun call(): String {
        while (true) {
            println("Task: Test")
            sleep(500)
        }
    }
}

fun main() {
    println("Main: Started")
    val executor = Executors.newCachedThreadPool() as ThreadPoolExecutor

    println("Main: Executing the Task")
    val futureResult: Future<String> = executor.submit(CancelledTask())
    sleep(2_000)

    println("Main: Canceling the task")
    // If the value of this parameter is true and the task is running, it will be canceled
    // If the value of the parameter is false and the task is running, it won't be canceled
    futureResult.cancel(true)

    println("Main: Canceled: ${futureResult.isCancelled}")
    println("Main: Done: ${futureResult.isDone}")

    executor.shutdown()
    println("Main: The executor has finished")
}
