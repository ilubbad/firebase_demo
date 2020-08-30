package com.app.contactapp.base

import junit.framework.Assert.assertEquals
import org.junit.Test


/**
Created by ibraheem lubbad on 8/30/20.
 **/
class TestMockResponseFileReader {
    @Test
    fun `read simple file`() {
        val reader = MockResponseFileReader("test.json")
        assertEquals(reader.content, "success")
    }
}