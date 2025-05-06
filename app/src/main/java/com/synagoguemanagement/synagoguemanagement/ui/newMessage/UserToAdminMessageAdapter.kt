package com.synagoguemanagement.synagoguemanagement.ui.newMessage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.synagoguemanagement.synagoguemanagement.R
import java.text.SimpleDateFormat
import java.util.*

class UserToAdminMessageAdapter(private val messages: MutableList<Messages>) :
    RecyclerView.Adapter<UserToAdminMessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val userEmailTextView: TextView = itemView.findViewById(R.id.userEmailTextView)
        val messageImageView: ImageView = itemView.findViewById(R.id.messageImageView)
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

        if (!message.imageBase64.isNullOrEmpty()) {
            val imageBytes = android.util.Base64.decode(message.imageBase64, android.util.Base64.DEFAULT)
            val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.messageImageView.setImageBitmap(bitmap)
            holder.messageImageView.visibility = View.VISIBLE
        } else {
            holder.messageImageView.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int = messages.size
}
