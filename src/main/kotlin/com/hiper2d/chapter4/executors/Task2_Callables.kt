package com.hiper2d.chapter4.executors

import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class FactorialCalculator(private val number: Int): Callable<Int> {
    override fun call(): Int {
        val result = when(number) {
            in 0..1 -> 1
            else -> (2..number).reduce { total, i ->
                sleep(20)
                total * i
            }
        }
        println("${Thread.currentThread().name}: result is $result")
        return result
    }
}

fun main(args: Array<String>) {
    val executor = Executors.newFixedThreadPool(2) as ThreadPoolExecutor
    val rand = Random()
    val futureResults = (1..10).map { executor.submit(FactorialCalculator(rand.nextInt(10))) }.toList()

    do {
        println("Main: Number of Completed Tasks: ${executor.completedTaskCount}")
        futureResults.forEachIndexed { index, future ->
            future.get()
            println("Main: Task $index is done ${future.isDone}")
        }
        sleep(50)
    } while (executor.completedTaskCount < futureResults.size)

    println("Main: Results")
    futureResults.forEachIndexed { index, future ->
        println("Main: Task $index: result is ${future.get()}")
    }

    executor.shutdown()
}
