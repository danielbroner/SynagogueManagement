package com.synagoguemanagement.synagoguemanagement.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.synagoguemanagement.synagoguemanagement.R
import java.io.ByteArrayOutputStream

class EditProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var profileImageView: ImageView
    private lateinit var chooseImageButton: Button
    private var encodedImage: String? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                profileImageView.setImageURI(it)
                saveImageToBase64()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        nameEditText = view.findViewById(R.id.edit_name)
        emailEditText = view.findViewById(R.id.edit_email)
        saveButton = view.findViewById(R.id.button_save)
        profileImageView = view.findViewById(R.id.edit_profile_image)
        chooseImageButton = view.findViewById(R.id.choose_image_button)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            nameEditText.setText(user.displayName)
            emailEditText.setText(user.email)

            // 🔽 Load Base64 image from Firestore
            FirebaseFirestore.getInstance().collection("users").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    val base64Image = doc.getString("profileImage")
                    if (!base64Image.isNullOrEmpty()) {
                        val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        profileImageView.setImageBitmap(bitmap)
                        encodedImage = base64Image
                    }
                }
        }

        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString().trim()
            val newEmail = emailEditText.text.toString().trim()

            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()

                user.updateProfile(profileUpdates).addOnCompleteListener { profileTask ->
                    if (profileTask.isSuccessful) {
                        user.updateEmail(newEmail).addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                // ✅ Save updates to Firestore
                                val updateMap = mutableMapOf(
                                    "name" to newName,
                                    "email" to newEmail
                                )
                                encodedImage?.let {
                                    updateMap["profileImage"] = it
                                }

                                FirebaseFirestore.getInstance().collection("users")
                                    .document(user.uid)
                                    .update(updateMap as Map<String, Any>)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Auth updated but Firestore failed", Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                Toast.makeText(context, "Failed to update email", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Failed to update name", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

    private fun saveImageToBase64() {
        val bitmap = (profileImageView.drawable as? BitmapDrawable)?.bitmap ?: return
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageBytes = baos.toByteArray()
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}
