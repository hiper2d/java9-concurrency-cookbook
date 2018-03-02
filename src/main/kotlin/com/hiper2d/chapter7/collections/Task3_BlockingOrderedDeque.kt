package com.hiper2d.chapter7.collections

import com.hiper2d.waitForThreads
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue

private const val EVENTS_PER_THREAD = 1_000

private class Event(val priority: Int, val thread: Int): Comparable<Event> {
    override fun compareTo(other: Event): Int = other.priority - priority
}

private class TaskEvent(val id: Int, val queue: BlockingQueue<Event>): Runnable {
    override fun run() {
        (1..EVENTS_PER_THREAD).forEach {
            queue.offer(Event(it, id))
        }
    }
}

fun main(args: Array<String>) {
    val queue = PriorityBlockingQueue<Event>()
    val threads = createAndStartThreads(queue)
    waitForThreads(threads)
    pollAllEventsForQueue(threads, queue)
    println("Main: Queue Size: ${queue.size}")
}

private fun pollAllEventsForQueue(threads: List<Thread>, queue: PriorityBlockingQueue<Event>) {
    (0 until threads.size * EVENTS_PER_THREAD).forEach {
        val evt = queue.poll()
        println("Thread ${evt.thread}: Priority ${evt.priority}")
    }
}

private fun createAndStartThreads(queue: PriorityBlockingQueue<Event>): List<Thread> = (1..5).map {
    val thr = Thread(TaskEvent(it, queue))
    thr.start()
    thr
}