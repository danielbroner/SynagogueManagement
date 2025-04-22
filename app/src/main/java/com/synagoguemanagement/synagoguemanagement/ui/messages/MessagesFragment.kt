package com.synagoguemanagement.synagoguemanagement.ui.messages

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.ui.messages.Message
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale


class MessagesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sendMessageButton: Button
    private lateinit var messagePopup: LinearLayout
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<Message>()

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
        messageEditText.setSingleLine(false)
        messageEditText.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        sendButton = view.findViewById(R.id.sendButton)

        // Populate initial messages if you want
        repeat(3) { i ->
            messages.add(Message("User $i", "2025-03-25", "This is message number $i"))
        }

        adapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        sendMessageButton.setOnClickListener {
            messagePopup.visibility = View.VISIBLE
            messageEditText.requestFocus()
            showKeyboard()
        }

        sendButton.setOnClickListener {
            val text = messageEditText.text.toString()
            if (text.isNotBlank()) {
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val newMessage = Message("", currentDate, text) // No name
                adapter.addMessage(newMessage)
                messageEditText.text.clear()
                messagePopup.visibility = View.GONE
                hideKeyboard()
            }
        }

    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(messageEditText.windowToken, 0)
    }
}
