package com.stark.sqldelightapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import db.persondb.Person

val diffUtil = object: DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }
}

class SqlDelightAdapter(
    private val onDelete: (Long) -> Unit,
    private val onEditClick: (Person) -> Unit
) : ListAdapter<Person, SqlDelightAdapter.SqlDelightViewHolder>(diffUtil) {

    class SqlDelightViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameTv = view.findViewById<TextView>(R.id.full_name_tv)
        val deleteBtn = view.findViewById<ImageButton>(R.id.deleteBtn)
        val editBtn = view.findViewById<ImageButton>(R.id.editBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SqlDelightViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val view = layoutInflator.inflate(R.layout.item_list, parent, false)
        return SqlDelightViewHolder(view)
    }

    override fun onBindViewHolder(holder: SqlDelightViewHolder, position: Int) {
        val person = getItem(position)

        holder.nameTv.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setMessage("Full Name = ${person.firstName.plus(" ${person.secondName}")}")
                .setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                .show()
        }
        holder.nameTv.text = person.firstName
        holder.deleteBtn.setOnClickListener {
            onDelete.invoke(person.id)
        }
        holder.editBtn.setOnClickListener {
            onEditClick.invoke(person)
        }
    }
}