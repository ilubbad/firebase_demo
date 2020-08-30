package com.app.contactapp.ui.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.app.contactapp.MainViewModel
import com.app.contactapp.ViewModelProviderFactory

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
open class BaseActivity : AppCompatActivity() {


    fun getViewModel(): MainViewModel =
        ViewModelProviders.of(this, ViewModelProviderFactory.getInstance(application))
            .get(MainViewModel::class.java)

}