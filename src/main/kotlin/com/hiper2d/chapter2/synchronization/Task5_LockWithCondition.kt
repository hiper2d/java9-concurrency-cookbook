package com.hiper2d.chapter2.synchronization

import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock


class FileMock(size: Int, length: Int) {
    private val rand = ThreadLocalRandom.current()
    private var content: Array<String> = Array(size) {
        (1..length).joinToString(separator = "") { nextLetter() }
    }
    private var index: Int = 0

    fun hasMoreLines() = index < content.size

    fun getLine(): String {
        println("File size: ${content.size - index}")
        return content[index++]
    }

    private fun nextLetter() = (rand.nextInt(25) + 97).toChar().toString()
}

class Buffer(private val maxSize: Int) {
    private val buffer: Queue<String> = LinkedList()
    private val lock = ReentrantLock()
    private val lines: Condition = lock.newCondition()
    private val space: Condition = lock.newCondition()
    private var pendingLines = true

    fun insert(line: String) {
        lock.lock()
        try {
            while (isFull()) {
                space.await()
            }
            buffer.offer(line)
            println("${Thread.currentThread().name}: Inserted Line: ${buffer.size}")
            lines.signalAll()
        } finally {
            lock.unlock()
        }
    }

    fun get(): String? {
        lock.lock()
        try {
            while (buffer.isEmpty() && hasPendingLineOrNonEmptyBuffer()) {
                lines.await()
            }
            if (hasPendingLineOrNonEmptyBuffer()) {
                val line = buffer.poll()
                println("${Thread.currentThread().name}: Line Read: ${buffer.size}")
                space.signalAll()
                return line
            }
        } finally {
            lock.unlock()
        }
        return null
    }

    private fun isFull() = buffer.size == maxSize

    @Synchronized fun setPendingLines(pendingLines: Boolean) {
        this.pendingLines = pendingLines
    }

    @Synchronized fun hasPendingLineOrNonEmptyBuffer() = pendingLines || buffer.isNotEmpty()
}

class Producer(private val mock: FileMock, private val buffer: Buffer): Runnable {
    override fun run() {
        buffer.setPendingLines(true)
        while (mock.hasMoreLines()) {
            buffer.insert(mock.getLine())
        }
        buffer.setPendingLines(false)
    }
}

class Consumer(private val buffer: Buffer): Runnable {
    private val rand = ThreadLocalRandom.current()

    override fun run() {
        while (buffer.hasPendingLineOrNonEmptyBuffer()) {
            val line = buffer.get()
            if (line != null) {
                processLine(line)
            }
        }
    }

    private fun processLine(line: String) {
        sleep(rand.nextInt(100).toLong())
    }
}

fun main(args: Array<String>) {
    val file = FileMock(100, 10)
    val buffer = Buffer(20)
    Thread(Producer(file, buffer), "Producer").start()
    (1..3).forEachIndexed { _, i ->
        Thread(Consumer(buffer), "Consumer $i").start()
    }
}