package com.app.contactapp.data

import android.content.Context
import com.app.contactapp.network.Rest.ApiClient
import com.app.contactapp.network.Rest.ApiInterface
import io.reactivex.Observable
import retrofit2.Response

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
class Repository(val mContext: Context) {
    companion object {
        private const val TAG = "Repository"
        private var instance: Repository? = null
        private lateinit var apiClient: ApiInterface

        @Synchronized
        fun getInstance(context: Context): Repository? {
            if (instance == null) {
                instance = Repository(context)
            }
            return instance
        }
    }

    init {
        apiClient = ApiClient.getApiClient(mContext)
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