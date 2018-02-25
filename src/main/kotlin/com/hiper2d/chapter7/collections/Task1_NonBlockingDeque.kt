package com.hiper2d.chapter7.collections

import java.util.concurrent.ConcurrentLinkedDeque

private class AddTask(val list: ConcurrentLinkedDeque<String>): Runnable {

    override fun run() {
        val threadName = Thread.currentThread().name
        (1..10_000).forEach {
            list.add("$threadName: Element $it")
        }
    }
}

private class PollTask(val list: ConcurrentLinkedDeque<String>): Runnable {

    override fun run() {
        (1..5_000).forEach {
            list.pollFirst()
            list.pollLast()
        }
    }
}

fun main(args: Array<String>) {
    val list = ConcurrentLinkedDeque<String>()

    val addTaskThreads = startHunderedAddTaskThreads(list)
    println("Main: ${addTaskThreads.size} AddTask threads have been lauched")

    waitForAllThread(addTaskThreads)
    println("Main: Size of the List: ${list.size}")

    val pollTaskThread = startHunderedPollTaskThreads(list)
    println("Main: ${pollTaskThread.size} PollTask threads have been lauched")

    waitForAllThread(pollTaskThread)
    println("Main: Size of the List: ${list.size}")
}

private fun startHunderedAddTaskThreads(list: ConcurrentLinkedDeque<String>): List<Thread> = (1..100).map {
    val thr = Thread(AddTask(list))
    thr.start()
    thr
}

private fun startHunderedPollTaskThreads(list: ConcurrentLinkedDeque<String>): List<Thread> = (1..100).map {
    val thr = Thread(PollTask(list))
    thr.start()
    thr
}

private fun waitForAllThread(thread: List<Thread>) {
    thread.forEach { it.join() }
}