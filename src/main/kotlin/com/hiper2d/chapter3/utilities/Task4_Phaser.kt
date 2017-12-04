package com.hiper2d.chapter3.utilities

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.Phaser

class FileSearch(
        private val initPath: Path,
        private val fileExtension: String,
        private val phaser: Phaser,
        private var results: MutableList<Path> = ArrayList()
): Runnable {
    override fun run() {
        phaser.arriveAndAwaitAdvance()
        println("${Thread.currentThread().name}: Starting.")

        if (Files.isDirectory(initPath)) directoryProcess(initPath)
        if (!checkResults()) return
        filterResults()
        if (!checkResults()) return
        showInfo()

        phaser.arriveAndDeregister()
        println("${Thread.currentThread().name}: Work completed.")
    }

    private fun directoryProcess(path: Path) {
        Files.newDirectoryStream(path).use {
            it.forEach {
                when(Files.isDirectory(it)) {
                    true -> directoryProcess(it)
                    false -> fileProcess(it)
                }
            }
        }
    }

    private fun fileProcess(path: Path) {
        if (path.fileName.toString().endsWith(fileExtension)) {
            results.add(path)
        }
    }

    private fun filterResults() {
        results = results.filter {
            val instant = Files.getLastModifiedTime(it).toInstant()
            LocalDateTime.ofInstant(instant, ZoneOffset.UTC) < LocalDateTime.now().minusDays(1)
        }.toMutableList()
    }

    private fun checkResults(): Boolean {
        return if (results.isEmpty()) {
            println("${Thread.currentThread().name}: Phase ${phaser.phase}: 0 results.")
            println("${Thread.currentThread().name}: Phase ${phaser.phase}: End.")
            phaser.arriveAndDeregister()
            false
        } else {
            println("${Thread.currentThread().name}: Phase ${phaser.phase}: ${results.size} results.")
            phaser.arriveAndAwaitAdvance()
            true
        }
    }

    private fun showInfo() {
        results.forEach {
            println("${Thread.currentThread().name}: ${it.toAbsolutePath()}")
        }
    }
}

fun main(args: Array<String>) {
    val phaser = Phaser(3)

    val mongo = FileSearch(Paths.get("D:\\Mongodb"), "log", phaser)
    val projects = FileSearch(Paths.get("D:\\Projects"), "log", phaser)
    val games = FileSearch(Paths.get("D:\\Games"), "log", phaser)

    val mongoThread = Thread(mongo,"Mongo")
    val projectsThread = Thread(projects,"Projects")
    val gamesThread = Thread(games,"Games")

    mongoThread.start()
    projectsThread.start()
    gamesThread.start()

    mongoThread.join()
    projectsThread.join()
    gamesThread.join()

    println("Terminated: ${phaser.isTerminated}")
}