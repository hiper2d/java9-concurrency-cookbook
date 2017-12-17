package com.hiper2d.chapter4.executors

import java.util.concurrent.*

class ExecutableTask(val name: String): Callable<String> {
    override fun call(): String {
        val duration = ThreadLocalRandom.current().nextLong(10)
        println("$name: Waiting $duration seconds for results.")
        TimeUnit.SECONDS.sleep(duration)
        return "Hello, I'm $name"
    }
}

class ResultFutureTask(callable: ExecutableTask): FutureTask<String>(callable) {
    private val name = callable.name

    override fun done() {
        println(when (isCancelled) {
            true -> "$name: Has been canceled"
            false -> "$name: Has finished"
        })
    }
}

fun main(args: Array<String>) {
    val executor = Executors.newCachedThreadPool()
    val futureTasks = createFiveFutureTasks(executor)

    TimeUnit.SECONDS.sleep(5)
    futureTasks.forEach { it.cancel(true) }

    futureTasks.filter { !it.isCancelled }.forEach { println(it.get()) }
    executor.shutdown()
}

private fun createFiveFutureTasks(executor: ExecutorService): Array<ResultFutureTask> {
    return Array(5) {
        val task = ExecutableTask("Task $it")
        val futureTask = ResultFutureTask(task)
        executor.submit(futureTask)
        futureTask
    }
}