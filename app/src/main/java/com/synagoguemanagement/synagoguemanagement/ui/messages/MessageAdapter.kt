package com.synagoguemanagement.synagoguemanagement.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

// ViewHolder to hold the view elements for each message item
class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    val messageTextView: TextView = view.findViewById(R.id.messageTextView)
}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    // Inflate the layout for each message item
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
    return MessageViewHolder(view)
}

override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    // Bind the message data to the views
    val message = messages[position]
    holder.nameTextView.text = message.name
    holder.dateTextView.text = message.date
    holder.messageTextView.text = message.message
}

override fun getItemCount(): Int = messages.size
}
