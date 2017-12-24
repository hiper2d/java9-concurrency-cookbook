package com.hiper2d.chapter4.executors

import java.time.LocalDateTime
import java.util.concurrent.*

private class Task(private val name: String): Runnable {
    private val initDate = LocalDateTime.now()

    override fun run() {
        println("${Thread.currentThread().name}: Task $name: Created on: $initDate")
        println("${Thread.currentThread().name}: Task $name: Started on: ${LocalDateTime.now()}")
        val duration = ThreadLocalRandom.current().nextLong(10)
        println("${Thread.currentThread().name}: Task $name: Doing a task during: $duration seconds")
        TimeUnit.SECONDS.sleep(duration)
        println("${Thread.currentThread().name}: Task $name: Finished on: ${LocalDateTime.now()}")
    }
}

private class RejectedTaskController: RejectedExecutionHandler {
    override fun rejectedExecution(r: Runnable, executor: ThreadPoolExecutor) {
        println("RejectedTaskController: The task $r has been rejected")
        println("RejectedTaskController: Executor $executor")
        println("RejectedTaskController: Terminating: ${executor.isTerminating}")
        println("RejectedTaskController: Terminated: ${executor.isTerminated}")
    }
}

private class Server {
    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) as ThreadPoolExecutor

    init {
        executor.rejectedExecutionHandler = RejectedTaskController()
    }

    fun executeTask(task: Task) {
        println("Server: A new task has arrived")
        executor.execute(task)
        println("Server: Pool Size: ${executor.poolSize}")
        println("Server: Active Count: ${executor.activeCount}")
        println("Server: Task Count: ${executor.taskCount}")
        println("Server: Completed Tasks: ${executor.completedTaskCount}")
    }

    fun endServer() {
        executor.shutdown()
    }
}

fun main(args: Array<String>) {
    val server = Server()
    println("Main: Starting.")
    (0 until 100).forEach {
        server.executeTask(Task("Task $it"))
    }
    println("Main: Shutting down the Executor.")
    server.endServer()

    println("Main: Sending another Task.")
    server.executeTask(Task("Rejected Task"))

    println("Main: End.")
}