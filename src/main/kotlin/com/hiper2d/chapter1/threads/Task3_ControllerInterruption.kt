package com.hiper2d.chapter1.threads

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit


class FileSearch(private val initPath: String, private val fileName: String): Runnable {
    override fun run() {
        try {
            directoryProcess(Paths.get(initPath))
        } catch (e: InterruptedException) {
            println("${Thread.currentThread().name}: The search was interrupted.")
        }
    }

    private fun directoryProcess(path: Path) {
        Files.newDirectoryStream(path).use { dirStream ->
            dirStream.forEach {
                when (Files.isDirectory(it)) {
                    true -> directoryProcess(it)
                    false -> fileProcess(it)
                }
                if (Thread.interrupted()) {
                    throw InterruptedException()
                }
            }
        }
    }

    private fun fileProcess(path: Path) {
        if (path.fileName.toString() == fileName) {
            println("${Thread.currentThread().name}: ${path.toAbsolutePath()}")
            TimeUnit.MILLISECONDS.sleep(500)
        }
        if (Thread.interrupted()) {
            throw InterruptedException()
        }
    }
}

fun main() {
    val thread = Thread(FileSearch("/Users/alexeyzelenovsky/Documents", "build.gradle.kts"))
    thread.start()
    TimeUnit.SECONDS.sleep(2)
    thread.interrupt()
}