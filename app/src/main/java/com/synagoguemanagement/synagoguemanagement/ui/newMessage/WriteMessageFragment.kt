package com.synagoguemanagement.synagoguemanagement.ui.newMessage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.synagoguemanagement.synagoguemanagement.R
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class WriteMessageFragment : Fragment() {

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var backButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserToAdminMessageAdapter

    private val messages = mutableListOf<Messages>()
    private val db = FirebaseFirestore.getInstance()
    private var selectedImageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_write_message, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        imagePreview = view.findViewById(R.id.imagePreview)
        recyclerView = view.findViewById(R.id.recyclerView)
        backButton = view.findViewById(R.id.backButton)

        adapter = UserToAdminMessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadMessages()

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        sendButton.setOnClickListener {
            val text = messageEditText.text.toString()
            val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown"

            if (selectedImageUri != null) {
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(selectedImageUri!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val compressedBitmap = compressBitmap(bitmap, 70) // Compress to 70% quality
                val base64Image = encodeBitmapToBase64(compressedBitmap)
                saveMessage(text, userEmail, base64Image)
            } else {
                saveMessage(text, userEmail, null)
            }
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            imagePreview.setImageURI(selectedImageUri)
            imagePreview.visibility = View.VISIBLE
        }
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveMessage(text: String, userEmail: String, base64Image: String?) {
        val message = Messages(Date(), text, userEmail, base64Image)
        db.collection("messages")
            .add(message)
            .addOnSuccessListener {
                messages.add(0, message)
                adapter.notifyItemInserted(0)
                messageEditText.text.clear()
                imagePreview.setImageDrawable(null)
                imagePreview.visibility = View.GONE
                selectedImageUri = null
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadMessages() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown"
        db.collection("messages")
            .whereEqualTo("userEmail", userEmail)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) return@addSnapshotListener
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
