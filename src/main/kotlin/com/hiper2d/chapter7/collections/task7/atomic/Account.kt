package com.hiper2d.chapter7.collections.task7.atomic

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.DoubleAccumulator
import java.util.concurrent.atomic.LongAdder

class Account {
    val operationsCount: LongAdder = LongAdder()
    val commission: DoubleAccumulator = DoubleAccumulator({ x, y -> x + y * .2 }, .0)
    val balance: AtomicLong = AtomicLong()

    constructor(): this(0)
    constructor(initialBalance: Long) {
        setBalance(initialBalance)
    }

    fun setBalance(value: Long) {
        balance.set(value)
        operationsCount.reset()
        commission.reset()
    }

    fun addAmount(amount: Long) {
        balance.getAndAdd(amount)
        operationsCount.increment()
        commission.accumulate(amount.toDouble())
    }

    fun substractAmount(amount: Long) {
        balance.getAndAdd(-amount)
        operationsCount.increment()
        commission.accumulate(amount.toDouble())
    }
}