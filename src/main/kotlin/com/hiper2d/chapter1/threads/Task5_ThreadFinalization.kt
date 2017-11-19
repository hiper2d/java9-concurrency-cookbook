package com.hiper2d.chapter1.threads

import java.util.*
import java.util.concurrent.TimeUnit


class DataSourcesLoader: Runnable {
    override fun run() {
        println("Beginning data source loading: ${Date()}")
        TimeUnit.SECONDS.sleep(4)
        println("Data source loading has finished: ${Date()}")
    }
}

class NetworkConnectionLoader: Runnable {
    override fun run() {
        println("Beginning network connection loading: ${Date()}")
        TimeUnit.SECONDS.sleep(6)
        println("Network connection loading has finished: ${Date()}")
    }
}

fun main(args: Array<String>) {
    val dsLoader = Thread(DataSourcesLoader(), "DataSourcesLoader")
    val ncLoader = Thread(NetworkConnectionLoader(), "NetworkConnectionLoader")

    dsLoader.start()
    ncLoader.start()

    dsLoader.join()
    ncLoader.join()
    println("Main configuration has been loaded")
}