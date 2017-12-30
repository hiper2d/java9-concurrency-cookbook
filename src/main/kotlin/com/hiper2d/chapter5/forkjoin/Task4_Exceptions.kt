package com.hiper2d.chapter5.forkjoin

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask
import java.util.concurrent.TimeUnit

private const val FORK_THRESHOLD = 10
private const val EXCEPTIONAL_INDEX = 4

class ExceptionalTask(
        private val array: Array<Int>,
        private val start: Int,
        private val end: Int
): RecursiveTask<Int>() {
    override fun compute(): Int {
        println("Task: Start from $start to $end")
        if (end - start < FORK_THRESHOLD) {
            if (EXCEPTIONAL_INDEX in (start..end)) {
                throw RuntimeException("This task throws an Exception: Task from $start to $end")
            }
            TimeUnit.SECONDS.sleep(1)
        } else {
            val mid = (end + start) / 2
            val task1 = ExceptionalTask(array, start, mid)
            val task2 = ExceptionalTask(array, mid, end)
            invokeAll(task1, task2)
            println("Task: Result form $start to $mid: ${task1.join()}")
            println("Task: Result form $mid to $end: ${task2.join()}")
        }
        println("Task: End form $start to $end")
        return 0
    }
}

fun main(args: Array<String>) {
    val arr = Array(100) { 0 }
    val task = ExceptionalTask(arr, 0, 100)
    val pool = ForkJoinPool.commonPool()

    pool.execute(task)
    pool.shutdown()

    pool.awaitTermination(1, TimeUnit.DAYS)
    if (task.isCompletedAbnormally) {
        println("Main: An exception has occurred")
        println("Main: ${task.exception}")
    }
    println("Main: Result: ${task.join()}")
}