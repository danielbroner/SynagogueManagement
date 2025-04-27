package com.synagoguemanagement.synagoguemanagement.ui.UserMessage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R

class UserMessageAdapter(private val messages: List<UserMessage>) :
    RecyclerView.Adapter<UserMessageAdapter.UserMessageViewHolder>() {

    class UserMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_message, parent, false)
        return UserMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserMessageViewHolder, position: Int) {
        val message = messages[position]
        holder.dateTextView.text = message.date
        holder.contentTextView.text = message.content
    }

    override fun getItemCount(): Int = messages.size
}
