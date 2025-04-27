package com.synagoguemanagement.synagoguemanagement.ui.newMessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.ui.messages.MessageAdapter

import java.util.Calendar

class AdminMessagesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserToAdminMessageAdapter
    private val messages = mutableListOf<Messages>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.adminRecyclerView)
        adapter = UserToAdminMessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadTodayMessages()
    }

    private fun loadTodayMessages() {
        val calendar = Calendar.getInstance()

        // Set the time to the start of the day (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Get the start of today as a Date object
        val startOfDay = calendar.time

        // Set the time to the end of the day (23:59:59)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        // Get the end of today as a Date object
        val endOfDay = calendar.time

        // Query Firestore for messages from today
        db.collection("messages")
            .whereGreaterThanOrEqualTo("date", startOfDay)
            .whereLessThanOrEqualTo("date", endOfDay)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    messages.clear()  // Clear the previous messages
                    for (document in snapshot.documents) {
                        val message = document.toObject(Messages::class.java)
                        message?.let { messages.add(it) }  // Add message to list
                    }
                    adapter.notifyDataSetChanged()  // Update the RecyclerView
                }
            }
    }
}


