package com.synagoguemanagement.synagoguemanagement.ui.UserMessage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.synagoguemanagement.synagoguemanagement.R

class UserMessageFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var plusButton: ImageButton
    private lateinit var adapter: UserMessageAdapter

    private val messages = mutableListOf<UserMessage>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView)
        plusButton = view.findViewById(R.id.plusButton) // Fix this part

        // Setup RecyclerView and adapter
        adapter = UserMessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Check admin flag
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isAdmin = prefs.getBoolean("is_admin", false)

        // Show or hide the plus button based on whether the user is admin or not
        plusButton.visibility = if (isAdmin) View.GONE else View.VISIBLE

        plusButton.setOnClickListener {
            // Navigate to the screen where the user can write a message
            findNavController().navigate(R.id.action_userMessageFragment_to_writeMessageFragment)
        }

        // Load messages based on whether the user is an admin or regular user
        loadMessages(isAdmin)
    }

    private fun loadMessages(isAdmin: Boolean) {
        val query = if (isAdmin) {
            db.collection("messages") // Admin sees all
        } else {
            val userEmail = requireContext()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_email", "") ?: ""
            db.collection("messages").whereEqualTo("userEmail", userEmail)
        }

        query.orderBy("date")
            .get()
            .addOnSuccessListener { result ->
                messages.clear()
                for (document in result) {
                    val message = document.toObject(UserMessage::class.java)
                    messages.add(0, message) // Most recent on top
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle error
            }
    }
}

