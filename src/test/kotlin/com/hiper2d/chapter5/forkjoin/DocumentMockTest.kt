package com.hiper2d.chapter5.forkjoin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DocumentMockTest {

    @Test
    fun generateDocument() {
        val mock = DocumentMock()
        val doc = mock.generateDocument(20, 20, "the")
        assertEquals(20, doc.size)
        assertEquals(20, doc[0].size)
    }
}
