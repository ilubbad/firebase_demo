package com.app.contactapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.contactapp.data.Repository
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
open class BaseActivity : AppCompatActivity() {
    val compositeDisposable = CompositeDisposable()
    private var mDataManager: Repository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataManager = Repository.getInstance(application.applicationContext)

    }

    open fun getDataManager(): Repository {
        return mDataManager!!
    }
}