package com.app.contactapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.app.contactapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
class LoginActivity : BaseActivity() {
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null


    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var account: GoogleSignInAccount
    lateinit var task: Task<GoogleSignInAccount>
    lateinit var credential: AuthCredential
    lateinit var gso: GoogleSignInOptions
    private val RC_SIGN_IN = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Set a listener for the login Button
        sign_in_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)

            }

        })


        auth = FirebaseAuth.getInstance()



        if (auth!!.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        // Configure Google Sign In
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        btnSignup = findViewById(R.id.btn_signup)
        btnLogin = findViewById(R.id.btn_login)
        btnReset = findViewById(R.id.btn_reset_password)
        progressBar = findViewById(R.id.progressBar)


        btnSignup!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity, SingupActivity::class.java))
        })
        btnReset!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))
        })
        btnLogin!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Please Entre your email.", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Please Enter your Password", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            progressBar!!.visibility = View.VISIBLE

            auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    progressBar!!.visibility = View.GONE

                    if (!task.isSuccessful) {
                        if (password.length < 6) {
                            inputPassword!!.error = getString(R.string.minimum_password)
                        } else {
                            Toast.makeText(
                                this@LoginActivity, getString(R.string.auth_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {

                        getSharedPreferences("contactapp", Context.MODE_PRIVATE)
                            .edit()
                            .putString("sign_in", "email")
                            .commit()

                        gotToNext()
                    }
                })
        })

    }

    private fun gotToNext() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

        }
        task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Toast.makeText(this@LoginActivity, "Google sign in failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth!!.currentUser
                    if (user != null) {
                        gotToNext()
                    }
                    getSharedPreferences("contactapp", Context.MODE_PRIVATE)
                        .edit()
                        .putString("sign_in", "google")
                        .commit()

                } else {
                    // Sign in fails
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()

                }
            }
    }


}
