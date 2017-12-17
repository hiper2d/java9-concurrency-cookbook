package com.hiper2d.chapter4.executors

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class Result(val name: String, val value: Int)

class ReduceTask(private val taskName: String) : Callable<Result> {
    override fun call(): Result {
        println("$taskName: Starting")
        val rand = ThreadLocalRandom.current()
        TimeUnit.SECONDS.sleep(rand.nextLong(10))
        val res = (1..5).reduce { _, _ -> rand.nextInt(100) }
        return Result(taskName, res)
    }
}

fun main(args: Array<String>) {
    val executor = Executors.newCachedThreadPool()

    val tasks = (1..10).map { ReduceTask("Taks $it") }.toList()
    val futureResult = executor.invokeAll(tasks)
    executor.shutdown()

    println("Main: Printing the results")
    futureResult.forEach {
        val result = it.get()
        println("${result.name}: ${result.value}")
    }
}