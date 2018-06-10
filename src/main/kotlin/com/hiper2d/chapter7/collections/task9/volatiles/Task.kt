package com.hiper2d.chapter7.collections.task9.volatiles

import com.hiper2d.logger

class Task(val flag: Flag): Runnable {
    companion object {
        val log = logger<Task>()
    }

    override fun run() {
        var i = 0
        while (flag.flag) {
            i++
        }
        log.debug("Stopped")
    }
}