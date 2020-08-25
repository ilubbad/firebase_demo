package com.app.contactapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
@Parcelize
data class Contact(
    var id: String?,
    val firstName: String,
    val lastName: String,
    val email: String
) : Parcelable