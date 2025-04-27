package com.synagoguemanagement.synagoguemanagement.ui.messages

import android.content.Context
import android.os.Bundle
import android.util.Log
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
                db.collection("message").add(message)
                messageEditText.text.clear()
                messagePopup.visibility = View.GONE
            }
        }

        // User: open Write Message screen
        plusButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, WriteMessageFragment())
                .addToBackStack(null) // Allows navigating back
                .commit()       }

        // Admin: open all received messages
        adminMessagesTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AdminMessagesFragment())
                .addToBackStack(null)  // Allows navigating back
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
                    doc.toObject(Message::class.java)?.let {
                        messages.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun deleteMessage(message: Message) {
        db.collection("message")
            .whereEqualTo("date", message.date)
            .whereEqualTo("content", message.message)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    doc.reference.delete()
                }
            }
    }
}
