package com.synagoguemanagement.synagoguemanagement.ui.newMessage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R
import java.text.SimpleDateFormat
import java.util.Locale

class UserToAdminMessageAdapter(private val messages: MutableList<Messages>) :
    RecyclerView.Adapter<UserToAdminMessageAdapter.MessageViewHolder>() {


    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val userEmailTextView: TextView = itemView.findViewById(R.id.userEmailTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_to_admin_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.dateTextView.text = sdf.format(message.date)
        holder.messageTextView.text = message.text
        holder.userEmailTextView.text = message.userEmail
    }

    override fun getItemCount(): Int = messages.size
}
