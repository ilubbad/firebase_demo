package com.app.contactapp

import android.app.Application
import androidx.lifecycle.*
import com.app.contactapp.data.Contact
import com.app.contactapp.data.Repository
import com.app.contactapp.network.Rest.ApiClient
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/**
Created by ibraheem lubbad on 8/30/20.
Copyright (c) 2020 he&She. All rights reserved.
 **/
class MainViewModel(val app: Application, val repository: Repository) : AndroidViewModel(app),
    LifecycleObserver {
    /**
     * Data members
     * Register information received from API
     */

    val isLoading = MutableLiveData<Boolean>()
    val contactCreated = MutableLiveData<Boolean>()
    val contactDeleted = MutableLiveData<Boolean>()
    val contactUpdated = MutableLiveData<Boolean>()
    val contactsLiveData = MutableLiveData<ArrayList<Contact>>()

    val contactLiveData = MutableLiveData<Contact>()

    /**
     * Manage Subscriptions
     */
    private var compositeDisposable = CompositeDisposable()


    fun changeUrlForTesting(url: String) {
        ApiClient.SERVER_URL = url

    }

    var IS_TESTING = false


    fun createNewContact(contact: Contact) {

        compositeDisposable.add(
            repository
                .createNewContact(contact)
                .subscribeOn(provideSuscriberOn())
                .observeOn(provideObserverOn())
                .doOnSubscribe { showLoading() }
                .doOnComplete { hideLoading() }
                .doOnError { hideLoading() }
                .subscribe({

                    contactCreated.value = true


                }) {
                    contactCreated.value = false

                }
        )
    }

    private val TAG = "MainViewModel"

    fun getAllContacts() {


        compositeDisposable.add(repository.getAllContact()
            .subscribeOn(
                provideSuscriberOn()
            )
            .observeOn(
                provideObserverOn()
            )
            .doOnSubscribe {
                showLoading()
            }
            .doOnComplete {
                hideLoading()
            }
            .doOnError { it ->

                hideLoading()
            }
            .subscribe({ resp ->
                contactsLiveData.value = resp

            }) { it ->
            })

    }

    fun getContactById(id: String) {


        compositeDisposable.add(repository.getContact(id)
            .subscribeOn(provideSuscriberOn())
            .observeOn(provideObserverOn())
            .doOnSubscribe { showLoading() }
            .doOnComplete { hideLoading() }
            .doOnError { hideLoading() }
            .subscribe({ resp ->
                contactLiveData.value = resp

            }) { _ ->

            })

    }


    fun deleteContact(id: String) {

        compositeDisposable.add(
            repository.deleteContact(id)
                .subscribeOn(provideSuscriberOn())
                .observeOn(provideObserverOn())
                .doOnSubscribe { showLoading() }
                .doOnComplete { hideLoading() }
                .doOnError { hideLoading() }
                .subscribe({
                    contactDeleted.value = true

                }) {
                    contactDeleted.value = false


                }
        )
    }


    fun updateContact(contact: Contact) {
        compositeDisposable.add(repository.updateContact(contact)
            .subscribeOn(provideSuscriberOn())
            .observeOn(provideObserverOn())
            .doOnSubscribe { showLoading() }
            .doOnComplete { hideLoading() }
            .doOnError { hideLoading() }
            .subscribe({

                contactUpdated.value = true

            }) { it ->
                contactUpdated.value = false


            }
        )
    }


    private fun hideLoading() {
        //if (!IS_TESTING)
        setIsLoading(false)
    }

    private fun showLoading() {
        //if (!IS_TESTING)
        setIsLoading(true)
    }

    private fun setIsLoading(b: Boolean) {
        isLoading.value = b
    }


    /**
     * OnStart, make our API calls and start listening for responses
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {

    }

    /**
     * OnStop, clean up subscriptions
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        compositeDisposable.clear()
        compositeDisposable = CompositeDisposable()
    }


    fun provideSuscriberOn(): Scheduler =
        if (IS_TESTING) Schedulers.trampoline() else Schedulers.io()

    fun provideObserverOn(): Scheduler =
        if (IS_TESTING) Schedulers.trampoline() else AndroidSchedulers.mainThread()


}