package com.hiper2d.chapter3.utilities

import java.lang.Thread.sleep
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadLocalRandom

class SeedGenerator(private val completableFuture: CompletableFuture<Int>): Runnable {
    override fun run() {
        println("SeedGenerator: Generating seed...")
        sleep(5_000)
        val seed = ThreadLocalRandom.current().nextInt(10)
        println("SeedGenerator: Seed generated: $seed")
        completableFuture.complete(seed)
    }
}

class NumberListGenerator(private val size: Int): () -> List<Long> {
    override fun invoke(): List<Long> {
        println("${Thread.currentThread().name} : NumberListGenerator : Start")
        val list = ArrayList<Long>()
        val rand = ThreadLocalRandom.current()
        (0 until size * 1_000_000).forEach {
            list.add(rand.nextLong(Math.round(Math.random() * Long.MAX_VALUE)))
        }
        println("${Thread.currentThread().name} : NumberListGenerator : End")
        return list
    }
}

class NumberSelector: (List<Long>) -> Long {
    override fun invoke(p1: List<Long>): Long {
        println("${Thread.currentThread().name}: Step 3: Start")
        val min = p1.min()?:0L
        val max = p1.max()?:0L
        val res = (min + max) / 2
        println("${Thread.currentThread().name}: Step 3: Result - $res")
        return res
    }
}

fun main(args: Array<String>) {
    val futureSeed = CompletableFuture<Int>()
    Thread(SeedGenerator(futureSeed)).start()
    println("Main: Getting the seed")
    val seed = futureSeed.get()
    println("Main: The seed is $seed")

    println("Main: Launching the list of numbers generator")
    val listGeneratorTask = NumberListGenerator(seed)
    val startFuture = CompletableFuture.supplyAsync(listGeneratorTask)

    println("Main: Launching step 1")
    val step1Future = startFuture.thenApplyAsync {
        println("${Thread.currentThread().name} Step 1: Start")
        println("${Thread.currentThread().name} Step 1: Result - ${findDistance(it)}")
    }

    println("Main: Launching step 2")
    val step2Future = startFuture.thenApplyAsync {
        println("${Thread.currentThread().name} Step 3: Start")
        it.max()?:0
    }
    val write2Future = step2Future.thenAccept { println("${Thread.currentThread().name} Step 2: Result - $it") }

    println("Main: Launching step 3")
    val step3Future = startFuture.thenApplyAsync(NumberSelector())

    println("Main: Waiting for the end of the three steps")
    val finalFuture = CompletableFuture.allOf(step1Future, write2Future, step3Future).thenAcceptAsync {
        println("Main: The CompletableFuture example has been completed")
    }

    finalFuture.get()
}

private fun findDistance(numbers: List<Long>): Long {
    var selectedDistance = Long.MAX_VALUE
    var selected = 0L
    numbers.forEach {
        val distance = Math.abs(it - 1_000)
        if (distance < selectedDistance) {
            selected = it
            selectedDistance = distance
        }
    }
    return selected
}