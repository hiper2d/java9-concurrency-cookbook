package com.hiper2d.chapter2.synchronization

import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

private class PricesInfo {

    private var lock: ReadWriteLock = ReentrantReadWriteLock()

    var price2: Double = 2.0
        get() {
            lock.readLock().lock()
            val value = field
            lock.readLock().unlock()
            return value
        }
        private set

    var price1: Double = 1.0
        get() {
            lock.readLock().lock()
            val value = field
            lock.readLock().unlock()
            return value
        }
        private set

    fun setPrices(price1: Double, price2: Double) {
        lock.writeLock().lock()
        println("${LocalDateTime.now()}: PricesInfo: Write Lock Acquired.")

        TimeUnit.SECONDS.sleep(3)
        this.price1 = price1
        this.price2 = price2

        println("${LocalDateTime.now()}: PricesInfo: Write Lock Released.")
        lock.writeLock().unlock()
    }
}

private class LockReader(private var pricesInfo: PricesInfo): Runnable {
    override fun run() {
        (1..20).forEach {
            println("${LocalDateTime.now()}: ${Thread.currentThread().name}: price1: ${pricesInfo.price1}")
            println("${LocalDateTime.now()}: ${Thread.currentThread().name}: price2: ${pricesInfo.price2}")
        }
    }
}

private class LockWriter(private var pricesInfo: PricesInfo): Runnable {
    private val rand = ThreadLocalRandom.current()

    override fun run() {
        (1..3).forEach {
            println("${LocalDateTime.now()}: Writer: Attempt to modify the prices.")
            pricesInfo.setPrices(rand.nextDouble() * 10, rand.nextDouble() * 10)
            println("${LocalDateTime.now()}: Writer: Prices have been modified.")
            TimeUnit.SECONDS.sleep(2)
        }
    }
}

fun main(args: Array<String>) {
    val info = PricesInfo()
    val reader = LockReader(info)
    val writer = LockWriter(info)

    Thread(writer).start()
    (1..5).forEach { Thread(reader).start() }
}