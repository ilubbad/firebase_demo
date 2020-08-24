package com.app.contactapp.ui

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
class ForgetPasswordActivity : BaseActivity() {

    private var email: EditText? = null
    private var btnresetPass: Button? = null
    private var btnback: Button? = null
    private var progressbar: ProgressBar? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        email = findViewById(R.id.email)
        btnresetPass = findViewById(R.id.btn_reset_password)
        btnback = findViewById(R.id.btn_back)
        progressbar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()



        btnback!!.setOnClickListener({
            finish()
        })
        btnresetPass!!.setOnClickListener(View.OnClickListener {
            val email = email!!.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter your email ", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            progressbar!!.visibility = View.VISIBLE

            auth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            "We have to sent you instraction in your email",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ForgetPasswordActivity, "Failed t sent to reset Email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    progressbar!!.visibility = View.GONE
                })

        })
    }
}
