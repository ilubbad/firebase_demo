package com.app.contactapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.app.contactapp.base.BaseTest
import com.app.contactapp.data.Contact
import com.app.contactapp.data.Repository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection


/**
Created by ibraheem lubbad on 8/30/20.
 **/
class MainUnitTest : BaseTest() {


    @Rule
    @JvmField
    val instantTaskExecutorRule =
        InstantTaskExecutorRule() // Force tests to be executed synchronously

    @Test
    fun getAllContacts() {
        // Prepare data

        this.mockHttpResponse("contacts_list.json", HttpURLConnection.HTTP_OK)
        // Pre-test
        assertEquals(
            "User should be null because stream not started yet",
            null,
            this.viewModel.contactsLiveData.value
        )
        // Execute View Model
        this.viewModel.getAllContacts()
        // Checks
        assertNotNull(
            "check if id fetch users operation success",
            this.viewModel.contactsLiveData.value
        )
        assertEquals("User list must same size", 3, this.viewModel.contactsLiveData.value?.size)
        assertEquals(
            "Should be reset to 'false' because stream ended",
            false,
            this.viewModel.isLoading.value
        )
    }

    @Test
    fun createNewContact_whenSuccess() {
        // Prepare data
        this.mockHttpResponse("test.json", HttpURLConnection.HTTP_OK)

        // Prepare data
        assertEquals(
            "User should be null because stream not started yet",
            null,
            this.viewModel.contactCreated.value
        )
        // Execute  get contact by id
        viewModel.createNewContact(Contact())
        // Checks
        assertEquals(
            "check if id create operation success",
            true,
            this.viewModel.contactCreated.value
        )

        assertEquals(
            "Should be reset to 'false' because stream ended",
            false,
            this.viewModel.isLoading.value
        )


    }

    @Test
    fun createNewContact_whenError() {
        // Prepare data
        this.mockHttpResponse("test.json", HttpURLConnection.HTTP_BAD_REQUEST)

        // Prepare data
        assertEquals(
            "User should be null because stream not started yet",
            null,
            this.viewModel.contactCreated.value
        )
        // Execute  get contact by id
        viewModel.createNewContact(Contact())
        // Checks
        assertEquals(
            "check if id create operation failed",
            false,
            this.viewModel.contactCreated.value
        )

        assertEquals(
            "Should be reset to 'false' because stream ended",
            false,
            this.viewModel.isLoading.value
        )


    }


    @Test
    fun updateContact() {
        // Prepare data
        this.mockHttpResponse("test.json", HttpURLConnection.HTTP_OK)


        // Prepare data
        assertEquals(
            "User should be null because stream not started yet",
            null,
            this.viewModel.contactUpdated.value
        )
        // Execute  get contact by id
        viewModel.updateContact(Contact())


        // Checks

        assertEquals(
            "check if id update operation success",
            true,
            this.viewModel.contactUpdated.value
        )

        assertEquals(
            "Should be reset to 'false' because stream ended",
            false,
            this.viewModel.isLoading.value
        )


    }

    val FACK_CONTACT_ID = "BQmThgoZK9rTGCEhwUcE"


    @Test
    fun getContactById() {

        // Prepare data

        this.mockHttpResponse("contact_by_id_success.json", HttpURLConnection.HTTP_OK)
        // Pre-test
        assertEquals(
            "User should be null because stream not started yet",
            null,
            this.viewModel.contactLiveData.value
        )
        // Execute  get contact by id
        viewModel.getContactById(FACK_CONTACT_ID)
        // Checks
        assertNotNull("operation success", this.viewModel.contactLiveData.value)

        assertEquals("check firstname", "ibraheem", this.viewModel.contactLiveData.value?.firstName)
        assertEquals("check last", "lubbad", this.viewModel.contactLiveData.value?.lastName)
        assertEquals(
            "check email",
            "ibraheem.a.lubbad@gmail.com",
            this.viewModel.contactLiveData.value?.email
        )
        assertEquals(
            "Should be reset to 'false' because stream ended",
            false,
            this.viewModel.isLoading.value
        )


    }


    @Test
    fun deleteContact() {
        this.mockHttpResponse("test.json", HttpURLConnection.HTTP_OK)

        // Pre-test
        assertEquals(
            "User should be null because stream not started yet",
            null,
            this.viewModel.contactDeleted.value
        )
        // Execute  get contact by id
        viewModel.deleteContact(FACK_CONTACT_ID)
        // Checks
        assertEquals(
            "check if id delete operation success",
            true,
            this.viewModel.contactDeleted.value
        )

        assertEquals(
            "Should be reset to 'false' because stream ended",
            false,
            this.viewModel.isLoading.value
        )


    }


    private lateinit var viewModel: MainViewModel


    private val lifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
    private val lifecycleRegistry = LifecycleRegistry(lifecycleOwner).also {
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(it)
    }

    private lateinit var repository: Repository

    @Mock
    private lateinit var context: Application

    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)
        setupContext()

        repository = Repository(context, getServerUrl())

        viewModel = MainViewModel(context, repository)

        viewModel.IS_TESTING = true

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleRegistry.addObserver(viewModel)
    }

    @After
    fun teardown() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        viewModel.IS_TESTING = false

    }

    private fun setupContext() {
        Mockito.`when`(context.applicationContext).thenReturn(context)
    }

    // OVERRIDING
    override fun isMockServerEnabled(): Boolean = true


}