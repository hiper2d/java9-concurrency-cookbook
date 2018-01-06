package com.hiper2d.chapter5.forkjoin

import com.hiper2d.mid
import java.lang.Thread.sleep
import java.util.concurrent.*

private class ArrayGenerator {
    companion object {
        fun generate(size: Int): Array<Int> {
            val rand = ThreadLocalRandom.current()
            return Array(size) { rand.nextInt(10) }
        }
    }
}

private class TaskManager {
    private val tasks = ConcurrentLinkedDeque<SearchNumberTask>()

    fun addTask(vararg task: SearchNumberTask) {
        task.forEach {
            tasks.add(it)
        }
    }

    fun cancelAllTasksExcept(activeTask: SearchNumberTask) {
        tasks.filter { it !== activeTask }.forEach { task ->
            task.cancel(true)
            println("Task ${task.start}:${task.end} cancelled")
        }
    }
}

private class SearchNumberTask(
        private val numbers: Array<Int>,
        val start: Int,
        val end: Int,
        private val searchNumber: Int,
        private val manager: TaskManager
): RecursiveTask<Int>() {
    override fun compute(): Int {
        println("Task: $start:$$end")
        return when {
            end - start < 10 -> lookForNumber()
            else -> launchTasks()
        }
    }

    private fun launchTasks(): Int {
        val mid = end.mid(start)
        val task1 = SearchNumberTask(numbers, start, mid, searchNumber, manager)
        val task2 = SearchNumberTask(numbers, mid, end, searchNumber, manager)
        manager.addTask(task1, task2)
        task1.fork()
        task2.fork()

        val firstTaskResult = task1.join()
        if (firstTaskResult != -1) {
            return firstTaskResult
        }
        return task2.join()
    }

    private fun lookForNumber(): Int {
        val position = (start until end).find {
            sleep(1_000)
            numbers[it] == searchNumber
        }
        return when (position) {
            null -> -1
            else -> {
                println("Task: Number $searchNumber found in position $position")
                manager.cancelAllTasksExcept(this)
                return position
            }
        }
    }
}

fun main(args: Array<String>) {
    val array = ArrayGenerator.generate(1000)
    val manager = TaskManager()
    val task = SearchNumberTask(array, 0, 1000, 5, manager)
    val pool = ForkJoinPool()

    pool.execute(task)
    pool.shutdown()

    pool.awaitTermination(1, TimeUnit.DAYS)
    println("Main: The program has finished")
}