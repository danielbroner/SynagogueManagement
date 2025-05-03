package com.synagoguemanagement.synagoguemanagement.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.synagoguemanagement.synagoguemanagement.MainActivity
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.auth.AuthManager
import com.synagoguemanagement.synagoguemanagement.ui.messages.MessagesFragment
import com.synagoguemanagement.synagoguemanagement.ui.signin.SignupFragment

class SigninFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        // Initialize UI elements
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val forgotPasswordText = view.findViewById<TextView>(R.id.forgotPasswordText)
        val signUpText = view.findViewById<TextView>(R.id.signUpText)

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                issueLogin(email, password)
            }
        }

        // Forgot password click listener
        forgotPasswordText.setOnClickListener {
            Toast.makeText(requireContext(), "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }

        // Sign Up click listener
        signUpText.setOnClickListener {
            openSignupPage()
        }

        return view
    }

    private fun issueLogin(email: String, password: String) {
        AuthManager.signInUser(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = AuthManager.getCurrentUser()?.uid
                    if (uid != null) {
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val isAdminFromEmail = email == "shay@test.com"
                                    val isAdmin = document.getBoolean("isAdmin") ?: isAdminFromEmail

                                    val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
                                    prefs.edit().putBoolean("is_admin", isAdmin).apply()

                                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                                    openMessagesPage()
                                    handleLoginSuccess()
                                } else {
                                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleLoginSuccess() {
        val mainActivity = activity as? MainActivity
        mainActivity?.onUserLoggedIn()
    }

    private fun openMessagesPage() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, MessagesFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun openSignupPage() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, SignupFragment())
            .addToBackStack(null)
            .commit()
    }
}
