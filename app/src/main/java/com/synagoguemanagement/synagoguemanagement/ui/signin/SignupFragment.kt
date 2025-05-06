package com.synagoguemanagement.synagoguemanagement.ui.signin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.auth.AuthManager
import java.io.ByteArrayOutputStream

class SignupFragment : Fragment() {

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val signInText = view.findViewById<TextView>(R.id.signInText)

        profileImageView = view.findViewById(R.id.profileImageView)
        chooseImageButton = view.findViewById(R.id.chooseImageButton)

        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(requireContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                registerNewUser(name, email, password)
            }
        }

        signInText.setOnClickListener {
            openLoginPage()
        }

        return view
    }

    private fun registerNewUser(name: String, email: String, password: String) {
        AuthManager.signUpUser(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = AuthManager.getCurrentUser()
                    val uid = user?.uid

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful && uid != null) {
                                val userData = hashMapOf(
                                    "email" to email,
                                    "name" to name,
                                    "isAdmin" to false,
                                    "profileImage" to (encodedImage ?: "")
                                )

                                val db = FirebaseFirestore.getInstance()
                                db.collection("users").document(uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                        openLoginPage()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "Failed to save user data", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(requireContext(), "Failed to update user profile", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveImageToBase64() {
        val bitmap = (profileImageView.drawable as? BitmapDrawable)?.bitmap ?: return
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageBytes = baos.toByteArray()
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun openLoginPage() {
        parentFragmentManager.popBackStack()
    }
}
