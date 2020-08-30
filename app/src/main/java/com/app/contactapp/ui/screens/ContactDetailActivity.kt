package com.app.contactapp.ui.screens

import android.os.Bundle
import com.app.contactapp.R
import com.app.contactapp.data.Contact
import kotlinx.android.synthetic.main.activity_contact_detail.*

class ContactDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)
        title = "Contact Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //show back button

        val contact = intent.getParcelableExtra<Contact>("contact")
        tvEmail.text = contact.email
        tvLastName.text = contact.lastName
        tvFirstName.text = contact.firstName

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}