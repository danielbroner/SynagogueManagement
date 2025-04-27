package com.synagoguemanagement.synagoguemanagement.ui.newMessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.ui.UserMessage.UserMessageAdapter
import java.util.Date

class WriteMessageFragment : Fragment() {

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserToAdminMessageAdapter
    private lateinit var backButton: ImageView

    private val messages = mutableListOf<Messages>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)
        recyclerView = view.findViewById(R.id.recyclerView)
        backButton = view.findViewById(R.id.backButton)

        adapter = UserToAdminMessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadMessages()

        sendButton.setOnClickListener {
            val text = messageEditText.text.toString()
            if (text.isNotBlank()) {
                val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown"
                val message = Messages(Date(), text, userEmail)

                db.collection("messages")
                    .add(message)
                    .addOnSuccessListener {
                        messages.add(0, message)
                        adapter.notifyItemInserted(0)
                        messageEditText.text.clear()
                    }
                    .addOnFailureListener {
                        // Handle error (optional Toast or Log)
                    }
            }
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadMessages() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown"

        db.collection("messages")
            .whereEqualTo("userEmail", userEmail)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    messages.clear()
                    for (document in snapshot.documents) {
                        val message = document.toObject(Messages::class.java)
                        message?.let { messages.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }
}

