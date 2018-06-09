package com.hiper2d.chapter7.collections.task7.atomic

import com.hiper2d.logger

class Bank(private val account: Account): Runnable {
    companion object {
        private val logger = logger<Bank>()

        fun runBankTransactions(account: Account) {
            (1..10_000).forEach { account.substractAmount(1000) }
            logger.debug("Finish bank transactions")
        }
    }

    override fun run() {
        runBankTransactions(account)
    }
}