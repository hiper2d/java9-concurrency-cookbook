package com.hiper2d.chapter2.synchronization

import java.lang.Thread.sleep
import java.util.concurrent.locks.StampedLock


class Position(var x: Int = 0, var y: Int = 0)

class Writer(private val position: Position, private val lock: StampedLock): Runnable {
    override fun run() {
        (1..10).forEach {
            val stamp = lock.writeLock()
            try {
                println("Writer: Lock acquired $stamp")
                position.x += 1
                position.y += 1
                sleep(1_000)
            } finally {
                lock.unlockWrite(stamp)
                println("Writer: Lock released $stamp")
            }

            sleep(1_000)
        }
    }
}

class Reader(private val position: Position, private val lock: StampedLock): Runnable {
    override fun run() {
        (1..50).forEach {
            val stamp = lock.readLock()
            try {
                println("Reader: $stamp - (${position.x}, ${position.y})")
                sleep(200)
            } finally {
                lock.unlockRead(stamp)
            }
        }
    }
}

class OptimisticReader(private val position: Position, private val lock: StampedLock): Runnable {
    override fun run() {
        (1..100).forEach {
            val stamp = lock.tryOptimisticRead()
            if (lock.validate(stamp)) {
                println("Optimistic Reader: $stamp - (${position.x}, ${position.y})")
            } else {
                println("Optimistic Reader: $stamp - Not Free")
            }
            sleep(200)
        }
    }
}

fun main(args: Array<String>) {
    val position = Position()
    val lock = StampedLock()

    val writer = Thread(Writer(position, lock))
    val reader = Thread(Reader(position, lock))
    val optimisticReader = Thread(OptimisticReader(position, lock))

    writer.start()
    reader.start()
    optimisticReader.start()

    writer.join()
    reader.join()
    optimisticReader.join()
}