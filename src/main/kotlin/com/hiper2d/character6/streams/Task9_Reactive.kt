package com.hiper2d.character6.streams

import java.util.concurrent.Flow
import java.util.concurrent.SubmissionPublisher

private data class Item(val title: String, val content: String)

private class Consumer1: Flow.Subscriber<Item> {
    override fun onComplete() {
        println("${Thread.currentThread().name}: Consumer 1: Completed")
    }

    override fun onSubscribe(p0: Flow.Subscription?) {
        println("${Thread.currentThread().name}: Consumer 1: Subscription received")
        println("${Thread.currentThread().name}: Consumer 1: No Items requested")
    }

    override fun onNext(p0: Item?) {
        println("${Thread.currentThread().name}: Consumer 1: Item received")
        println("${Thread.currentThread().name}: Consumer 1: ${p0?.title}")
        println("${Thread.currentThread().name}: Consumer 1: ${p0?.content}")
    }

    override fun onError(p0: Throwable?) {
        println("${Thread.currentThread().name}: Consumer 1: Error")
    }
}

private class Consumer2: Flow.Subscriber<Item> {
    private var subscription: Flow.Subscription? = null

    override fun onComplete() {
        println("${Thread.currentThread().name}: Consumer 2: Completed")
    }

    override fun onSubscribe(p0: Flow.Subscription) {
        println("${Thread.currentThread().name}: Consumer 2: Subscription received")
        subscription = p0
        subscription?.request(1)
    }

    override fun onNext(p0: Item?) {
        println("${Thread.currentThread().name}: Consumer 2: Item received")
        println("${Thread.currentThread().name}: Consumer 2: ${p0?.title}")
        println("${Thread.currentThread().name}: Consumer 2: ${p0?.content}")
        subscription?.request(1)
    }

    override fun onError(p0: Throwable?) {
        println("${Thread.currentThread().name}: Consumer 2: Error")
    }
}

private class Consumer3: Flow.Subscriber<Item> {
    override fun onComplete() {
        println("${Thread.currentThread().name}: Consumer 3: Completed")
    }

    override fun onSubscribe(p0: Flow.Subscription?) {
        println("${Thread.currentThread().name}: Consumer 3: Subscription received")
        p0?.request(3)
    }

    override fun onNext(p0: Item?) {
        println("${Thread.currentThread().name}: Consumer 3: Item received")
        println("${Thread.currentThread().name}: Consumer 3: ${p0?.title}")
        println("${Thread.currentThread().name}: Consumer 3: ${p0?.content}")
    }

    override fun onError(p0: Throwable?) {
        println("${Thread.currentThread().name}: Consumer 3: Error")
    }
}

fun main(args: Array<String>) {
    val consumer1 = Consumer1()
    val consumer2 = Consumer2()
    val consumer3 = Consumer3()

    val publisher = SubmissionPublisher<Item>()
    publisher.subscribe(consumer1)
    publisher.subscribe(consumer2)
    publisher.subscribe(consumer3)

    (1..10).forEach {
        publisher.submit(Item("Item$it", "This is the Item number $it"))
        Thread.sleep(1_000)
    }
    publisher.close()
}