package com.app.contactapp


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.contactapp.data.Repository


class ViewModelProviderFactory
constructor(val app: Application, val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var INSTANCE: ViewModelProviderFactory? = null

        fun getInstance(application: Application): ViewModelProviderFactory {
            return INSTANCE ?: synchronized(ViewModelProviderFactory::class.java) {
                ViewModelProviderFactory(
                    application,
                    Repository(application)
                ).also { INSTANCE = it }
            }
        }
    }

}