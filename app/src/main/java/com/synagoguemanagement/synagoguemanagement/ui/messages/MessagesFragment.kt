package com.synagoguemanagement.synagoguemanagement.ui.messages

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.ui.newMessage.AdminMessagesFragment
import com.synagoguemanagement.synagoguemanagement.ui.newMessage.WriteMessageFragment
import java.text.SimpleDateFormat
import java.util.*

class MessagesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sendButton: Button
    private lateinit var messageEditText: EditText
    private lateinit var messagePopup: LinearLayout
    private lateinit var sendMessageButton: Button

    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    private val db = FirebaseFirestore.getInstance()
    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        sendMessageButton = view.findViewById(R.id.sendMessageButton)
        messagePopup = view.findViewById(R.id.messagePopup)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)

        val plusButton = view.findViewById<ImageButton>(R.id.plusButton)
        val adminMessagesTextView = view.findViewById<TextView>(R.id.adminMessagesTextView)

        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        isAdmin = prefs.getBoolean("is_admin", false)

        adapter = MessageAdapter(messages, isAdmin) { messageToDelete ->
            if (isAdmin) {
                deleteMessage(messageToDelete)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        if (isAdmin) {
            // Admin sees admin popup button and "View All Messages" text
            sendMessageButton.visibility = View.VISIBLE
            adminMessagesTextView.visibility = View.VISIBLE
            plusButton.visibility = View.GONE
        } else {
            // User sees only the "+" button
            sendMessageButton.visibility = View.GONE
            messagePopup.visibility = View.GONE
            adminMessagesTextView.visibility = View.GONE
            plusButton.visibility = View.VISIBLE
        }

        // Admin: opens popup to write message
        sendMessageButton.setOnClickListener {
            messagePopup.visibility = View.VISIBLE
        }

        // Admin: sends the message from popup
        sendButton.setOnClickListener {
            val content = messageEditText.text.toString().trim()
            if (content.isNotEmpty()) {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
                val message = Message("", date, content)
                db.collection("message")
                    .add(message)
                    .addOnSuccessListener {
                        messageEditText.text.clear()
                        messagePopup.visibility = View.GONE
                        Toast.makeText(requireContext(), "Message sent successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to send message.", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // User: open Write Message screen
        plusButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, WriteMessageFragment())
                .addToBackStack(null)
                .commit()
        }

        // Admin: open all received messages
        adminMessagesTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AdminMessagesFragment())
                .addToBackStack(null)
                .commit()
        }

        listenForMessages()
    }

    private fun listenForMessages() {
        db.collection("message")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                messages.clear()
                for (doc in snapshot.documents) {
                    doc.toObject(Message::class.java)?.let { message ->
                        message.id = doc.id // Save document ID inside the message
                        messages.add(message)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun deleteMessage(message: Message) {
        if (message.id.isNotEmpty()) {
            // Show confirmation dialog
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Message")
            builder.setMessage("Are you sure you want to delete this message?")
            builder.setPositiveButton("Delete") { _, _ ->
                // If user confirms
                db.collection("message").document(message.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Message deleted", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to delete message", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
    }

}
