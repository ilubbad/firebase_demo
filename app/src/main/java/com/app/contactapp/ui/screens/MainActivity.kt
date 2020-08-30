package com.app.contactapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.contactapp.R
import com.app.contactapp.data.Contact
import com.app.contactapp.ui.adapters.ContactAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
class MainActivity : BaseActivity() {

    private var email: TextView? = null


    private var progressBar: ProgressBar? = null
    private var rvItems: RecyclerView? = null
    private var auth: FirebaseAuth? = null
    val user = FirebaseAuth.getInstance().currentUser

    // this listener will be called when there is change in firebase user session
    internal var authListener: FirebaseAuth.AuthStateListener? =
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                setDataToView(user)

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get firebase auth instance
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.useremail)
        rvItems = findViewById(R.id.rvItems)
        progressBar = findViewById(R.id.progressBar)

        getViewModel().isLoading.observe(this, {
            if (getViewModel().IS_TESTING) return@observe
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }

        })




        getAllContact()
        //get current user

        setDataToView(user!!)

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }


    }

    fun showLoading() {
        progressBar!!.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progressBar!!.visibility = View.GONE
    }

    lateinit var dialogShowContact: AlertDialog
    private fun showContactDailog(contact: Contact?) {


        val inflater = layoutInflater
        val dialoglayout: View = inflater.inflate(R.layout.dialog, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialoglayout)
        dialogShowContact = builder.create()
        val edEmail = dialoglayout.findViewById<EditText>(R.id.edEmail)
        val edFirstName = dialoglayout.findViewById<EditText>(R.id.edFirstName)
        val edLastName = dialoglayout.findViewById<EditText>(R.id.edLastName)
        val btnSave = dialoglayout.findViewById<Button>(R.id.btnSave)
        val tvTitle = dialoglayout.findViewById<TextView>(R.id.tvTitle)

        contact?.let {
            tvTitle.setText(R.string.update_contact)
            edEmail.setText(contact.email)
            edFirstName.setText(contact.firstName)
            edLastName.setText(contact.lastName)
            btnSave.setText(R.string.update)
        }


        btnSave.setOnClickListener {

            val email = edEmail!!.text.toString().trim()
            val firstName = edFirstName!!.text.toString().trim()
            val lastName = edLastName!!.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, getString(R.string.err_email), Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(firstName)) {
                Toast.makeText(applicationContext, getString(R.string.err_name), Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(lastName)) {
                Toast.makeText(applicationContext, getString(R.string.err_name), Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }


            if (contact == null) {
                createContact(Contact(null, firstName, lastName, email))
            } else {
                updateContact(Contact(contact.id, firstName, lastName, email))
            }


        }

        dialogShowContact.show()

    }

    private val TAG = "MainActivity"
    private fun createContact(
        contact: Contact
    ) {


        getViewModel().createNewContact(contact)

        getViewModel().contactCreated.observe(this, Observer {
            if (it) {
                hideLoading()
                Toast.makeText(this, "Created ", Toast.LENGTH_LONG).show()
                dialogShowContact.dismiss()


                getAllContact()
            }
        })

    }

    private fun updateContact(contact: Contact) {

        getViewModel().updateContact(contact)
        getViewModel().contactUpdated.observe(this, {
            dialogShowContact.dismiss()
            getAllContact()
        })


    }



    private fun deleteContact(id: String) {
        showLoading()
        getViewModel().deleteContact(id)
        getViewModel().contactDeleted.observe(this, {
            if (it) {
                getAllContact()
            }
        })

    }

    private fun getAllContact() {

        getViewModel().getAllContacts()
        getViewModel().contactsLiveData.observe(this, Observer {
            rvItems?.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter =
                    ContactAdapter(this@MainActivity, it, object : ContactAdapter.AdapterActions {
                        override fun deleteContact(contact: Contact) {
                            showDeleteDialog(contact)
                        }

                        override fun editContact(contact: Contact) {
                            showContactDailog(contact)
                        }

                        override fun openDetail(contact: Contact) {
                            val intent =
                                Intent(this@MainActivity, ContactDetailActivity::class.java)
                            intent.putExtra("contact", contact)
                            startActivity(intent)
                        }


                    })
            }
        })

    }

    private fun showDeleteDialog(contact: Contact) {


        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle(getString(R.string.delete_contact))
            setMessage(getString(R.string.delete_contact_message))
            setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.yes)) { dialogInterface, _ ->
                    deleteContact(contact.id!!)
                    dialogInterface.dismiss()

                }
                .setNegativeButton(getString(R.string.no)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
        }


        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


    @SuppressLint("SetTextI18n")
    private fun setDataToView(user: FirebaseUser) {

        email!!.text = "User Email: " + user.email!!


    }

    fun removeUser() {
        showLoading()
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@MainActivity,
                    "Your profile is deleted:( Create a account now!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@MainActivity, SingupActivity::class.java))
                finish()
                hideLoading()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Failed to delete your account!",
                    Toast.LENGTH_SHORT
                ).show()
                hideLoading()
            }
        }

    }

    //sign out method
    fun signOut() {
        auth!!.signOut()


        // this listener will be called when there is change in firebase user session
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    public override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val isGoogleSignIn = getSharedPreferences("contactapp", Context.MODE_PRIVATE)
            .getString("sign_in", "").equals("google")
        menu!!.findItem(R.id.btnChangePassword).isVisible = !isGoogleSignIn

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnChangePassword -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))

            }
            R.id.btnRemoveUser -> {
                removeUser()
            }
            R.id.btnSingOut -> {
                signOut()

            }

            R.id.btnAdd -> {
                showContactDailog(null)

            }


        }
        return super.onOptionsItemSelected(item)
    }


}