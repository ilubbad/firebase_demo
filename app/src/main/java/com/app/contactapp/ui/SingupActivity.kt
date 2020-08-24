package com.app.contactapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.app.contactapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
class SingupActivity : BaseActivity() {
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: Button? = null
    private var progressBar: ProgressBar? = null

    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

//        add my code
        auth = FirebaseAuth.getInstance()

        btnSignIn = findViewById<Button>(R.id.sign_in_button)
        btnSignUp = findViewById<Button>(R.id.sign_up_button)
        inputEmail = findViewById<EditText>(R.id.email)
        inputPassword = findViewById<EditText>(R.id.password)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        btnResetPassword = findViewById<Button>(R.id.btn_reset_password)

        btnResetPassword!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@SingupActivity, ForgetPasswordActivity::class.java))
        })

        btnSignIn!!.setOnClickListener(View.OnClickListener {
            finish()
        })
        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, getString(R.string.err_email), Toast.LENGTH_LONG)
                    .show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.err_password),
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.err_password_validation),
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }
            progressBar!!.visibility = View.VISIBLE

            //create user
            auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task ->

                    progressBar!!.visibility = View.GONE

                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@SingupActivity,
                            getString(R.string.user_not_exist),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnCompleteListener
                    } else {
                        val intent = Intent(this@SingupActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                        finish()
                    }


                })

        })
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }
}
