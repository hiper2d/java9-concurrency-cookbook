package com.hiper2d.chapter7.collections.task7.atomic

import com.hiper2d.logger

class Company(private val account: Account): Runnable {
    companion object {
        private val logger = logger<Company>()

        fun runCompanyTransactions(account: Account) {
            (1..10_000).forEach { account.addAmount(1000) }
            logger.debug("Finish company transactions")
        }
    }

    override fun run() {
        runCompanyTransactions(account)
    }
}

