package com.app.contactapp.base


import com.app.contactapp.network.Rest.ApiClient.SERVER_URL
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

/**
Created by ibraheem lubbad on 8/30/20.
 **/
abstract class BaseTest {

    lateinit var mockServer: MockWebServer

    @Before
    open fun setUp() {
        this.configureMockServer()

    }

    @After
    open fun tearDown() {
        this.stopMockServer()
    }

    fun getServerUrl(): String {
        return if (isMockServerEnabled()) mockServer.url("/").toString() else SERVER_URL
    }

    // MOCK SERVER
    abstract fun isMockServerEnabled(): Boolean // Because we don't want it always enabled on all tests

    open fun configureMockServer() {
        if (isMockServerEnabled()) {
            mockServer = MockWebServer()
            mockServer.start()
        }
    }

    open fun stopMockServer() {
        if (isMockServerEnabled()) {
            mockServer.shutdown()
        }
    }

    open fun mockHttpResponse(fileName: String, responseCode: Int) = mockServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(MockResponseFileReader(fileName).content)
    )


}