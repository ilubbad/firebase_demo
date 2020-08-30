package com.app.contactapp.base

import java.io.InputStreamReader


/**
Created by ibraheem lubbad on 8/30/20.
 **/
class MockResponseFileReader(path: String) {
    val content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}