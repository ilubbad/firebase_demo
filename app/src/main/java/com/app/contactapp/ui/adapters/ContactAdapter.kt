package com.app.contactapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.contactapp.R
import com.app.contactapp.data.Contact
import kotlinx.android.synthetic.main.item_contact.view.*


/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
class ContactAdapter(
    val context: Context,
    val list: ArrayList<Contact>,
    val listener: AdapterActions
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {


    interface AdapterActions {
        fun deleteContact(contact: Contact)
        fun editContact(contact: Contact)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Contact, listener: AdapterActions) = with(itemView) {
            tvLastName.text = item.lastName
            tvFirstName.text = item.firstName
            tvEmail.text = item.email
            btnDelete.setOnClickListener { listener.deleteContact(item) }
            btnEdit.setOnClickListener { listener.editContact(item) }
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(list[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
