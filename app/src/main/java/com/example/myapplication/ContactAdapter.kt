package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private val context: Context,
    private val data: MutableList<Pair<String, String>>,
    private val editListener: (Int) -> Unit,
    private val deleteListener: (Int) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = data[position]
        holder.nameTextView.text = "姓名：${contact.first}"
        holder.telTextView.text = "電話：${contact.second}"

        holder.editImageView.setOnClickListener {
            editListener(position)
        }

        holder.trashImageView.setOnClickListener {
            deleteListener(position)
        }
    }

    override fun getItemCount(): Int = data.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val telTextView: TextView = itemView.findViewById(R.id.tel)
        val editImageView: ImageView = itemView.findViewById(R.id.edit)
        val trashImageView: ImageView = itemView.findViewById(R.id.trash)
    }
}
