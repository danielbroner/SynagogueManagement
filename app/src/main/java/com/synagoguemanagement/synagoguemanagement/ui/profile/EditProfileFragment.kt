package com.synagoguemanagement.synagoguemanagement.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        nameEditText = view.findViewById(R.id.edit_name)
        emailEditText = view.findViewById(R.id.edit_email)
        saveButton = view.findViewById(R.id.button_save)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            nameEditText.setText(user.displayName)
            emailEditText.setText(user.email)
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

                                // ✅ Update Firestore name too
                                val db = FirebaseFirestore.getInstance()
                                db.collection("users").document(user.uid)
                                    .update("name", newName, "email", newEmail)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Profile updated in Auth but not in Firestore", Toast.LENGTH_SHORT).show()
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
}


