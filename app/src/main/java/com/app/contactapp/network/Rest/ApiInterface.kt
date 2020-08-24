package com.app.contactapp.network.Rest

import com.app.contactapp.data.Contact
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
interface ApiInterface {
    /**
     * all api are uesed in application
     */
    /*

        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("email") email: String?

     */
    @FormUrlEncoded
    @POST("contacts")
    fun createNewContact(
        @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("email") email: String?
    ): Observable<String>

    @FormUrlEncoded
    @PATCH("contacts/{contactId}")
    fun updateContact(
        @Path("contactId") contactId: String?, @Field("firstName") firstName: String?,
        @Field("lastName") lastName: String?,
        @Field("email") email: String
    ): Observable<Response<Void>>


    @GET("contacts/{contactId}")
    fun getContact(
        @Path("contactId") contactId: String?
    ): Observable<Contact>

    @GET("contacts")
    fun getAllContact(): Observable<ArrayList<Contact>>


    @DELETE("contacts/{contactId}")
    fun deleteContact(
        @Path("contactId") contactId: String?
    ): Observable<Response<Void>>
}