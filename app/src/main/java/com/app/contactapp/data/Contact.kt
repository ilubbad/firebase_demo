package com.app.contactapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
@Parcelize
data class Contact(
    var id: String? = "BQmThgoZK9rTGCEhwUcE",
    val firstName: String = "ibraheem",
    val lastName: String = "lubbad",
    val email: String = "ibraheem.a.lubbad@gmail.com"
) : Parcelable