package com.app.contactapp.data

import android.app.Application
import com.app.contactapp.network.Rest.ApiClient
import com.app.contactapp.network.Rest.ApiInterface
import io.reactivex.Observable
import retrofit2.Response

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
class Repository(val app: Application, val serverUrl: String = ApiClient.SERVER_URL) {
    companion object {
        private const val TAG = "Repository"
        private var instance: Repository? = null
        private lateinit var apiClient: ApiInterface

        @Synchronized
        fun getInstance(app: Application, serverUrl: String = ApiClient.SERVER_URL): Repository {
            if (instance == null) {
                instance = Repository(app, serverUrl)
            }
            return instance as Repository
        }
    }

    init {
        apiClient = ApiClient.getApiClient(app, serverUrl)
    }

    fun createNewContact(contact: Contact): Observable<String> {
        return apiClient.createNewContact(contact.firstName, contact.lastName, contact.email)
    }

    fun updateContact(contact: Contact): Observable<Response<Void>> {
        return apiClient.updateContact(
            contact.id,
            contact.firstName,
            contact.lastName,
            contact.email
        )
    }

    fun getContact(id: String): Observable<Contact> {
        return apiClient.getContact(id)
    }

    fun deleteContact(id: String): Observable<Response<Void>> {
        return apiClient.deleteContact(id)
    }

    fun getAllContact(): Observable<ArrayList<Contact>> {
        return apiClient.getAllContact()
    }

}