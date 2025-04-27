package com.synagoguemanagement.synagoguemanagement.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R

class MessageAdapter(
    private val messages: MutableList<Message>,
    private val isAdmin: Boolean,
    private val onDeleteMessage: ((Message) -> Unit)? = null
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {


    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.dateTextView.text = message.date
        holder.messageTextView.text = message.message

        if (isAdmin) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener {
                messages.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, messages.size)
            }
        } else {
            holder.deleteButton.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(0, message)
        notifyItemInserted(0)
    }
}
