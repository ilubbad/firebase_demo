package com.app.contactapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.app.contactapp.R
import com.google.firebase.auth.EmailAuthProvider.getCredential
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
class ChangePasswordActivity : BaseActivity() {

    private var useremail: TextView? = null
    private var newPassword: EditText? = null
    private var oldPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var changePass: Button? = null
    val user = FirebaseAuth.getInstance().currentUser

    private val TAG = "ChangePasswordActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        useremail = findViewById<View>(R.id.useremail) as TextView?
        progressBar = findViewById(R.id.progressBar)

        useremail!!.text = "User Email: " + user?.email!!

        newPassword = findViewById<View>(R.id.newPassword) as EditText?
        oldPassword = findViewById<View>(R.id.oldPassword) as EditText?
        changePass = findViewById<View>(R.id.changePass) as Button?


        changePass!!.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            if (newPassword!!.text.toString().trim { it <= ' ' } != "") {
                if (newPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
                    newPassword!!.error = "Password too short, enter minimum 6 characters"
                    progressBar!!.visibility = View.GONE
                } else {


// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.


// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(getCredential(user.email!!, oldPassword!!.text.toString()))
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                user.updatePassword(newPassword!!.text.toString().trim())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this@ChangePasswordActivity,
                                                "Password is updated, sign in with new password!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            signOut()
                                            progressBar!!.visibility = View.GONE
                                        } else {
                                            Toast.makeText(
                                                this@ChangePasswordActivity,
                                                "Failed to update password!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            progressBar!!.visibility = View.GONE
                                        }
                                    }


                            } else {

                                Toast.makeText(this, "Error auth failed", Toast.LENGTH_LONG).show()
                            }
                        }


                }
            } else if (newPassword!!.text.toString().trim { it <= ' ' } == "") {
                newPassword!!.error = "Enter password"
                progressBar!!.visibility = View.GONE
            }
        }


    }

    //sign out method
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.d(TAG, "firebaseAuth: " + user)

            if (user == null) {
                startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

}