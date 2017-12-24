package com.hiper2d.chapter5.forkjoin

import java.lang.Thread.sleep
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveAction

private class Product(val name: String, var price: Double)

private class ProductListGenerator(private val size: Int) {
    fun generate(): List<Product> = (0 until size).map { Product("Product $it", 10.0) }.toList()
}

private class PriceIncrementTask(
        private val products: List<Product>,
        private val first: Int,
        private val last: Int,
        private val increment: Double
): RecursiveAction() {
    override fun compute() {
        if (last - first <= 10) {
            updatePrices()
        } else {
            println("Task: Pending tasks: ${getQueuedTaskCount()}")
            val middle = (first + last) / 2
            val task1 = PriceIncrementTask(products, first, middle, increment)
            val task2 = PriceIncrementTask(products, middle + 1, last, increment)
            invokeAll(task1, task2)
        }
    }

    private fun updatePrices() {
        (first..last).forEach { products[it].price += increment }
    }
}

fun main(args: Array<String>) {
    val products = ProductListGenerator(10_000).generate()
    val task = PriceIncrementTask(products, 0, products.lastIndex, 2.0)
    val pool = ForkJoinPool()

    pool.execute(task)
    waitUtillTaskIsDone(pool, task)
    pool.shutdown()

    if (task.isCompletedNormally) {
        println("Main: The process has completed normally.")
    }
    products.filter { it.price != 12.0 }.forEach { println("Product: ${it.name}") }

    println("Main: End of the program.")
}

private fun waitUtillTaskIsDone(pool: ForkJoinPool, task: PriceIncrementTask) {
    do {
        println("Main: Thread Count ${pool.activeThreadCount}")
        println("Main: Thread Steal ${pool.stealCount}")
        println("Main: Parallelism ${pool.parallelism}")
        sleep(5)
    } while (!task.isDone)
}
