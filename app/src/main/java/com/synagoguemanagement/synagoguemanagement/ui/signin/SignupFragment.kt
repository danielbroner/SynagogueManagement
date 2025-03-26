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
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.auth.AuthManager

class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // Initialize UI elements
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val signInText = view.findViewById<TextView>(R.id.signInText)

        // Register button click listener
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
                registerNewUser(email, password)
            }
        }

        // Sign In text click listener (to navigate to SignInFragment)
        signInText.setOnClickListener {
            openLoginPage()
        }

        return view
    }

    private fun registerNewUser(email: String, password: String) {
        //TODO Add user to DB
        AuthManager.signUpUser(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireActivity(), "Sign-up successful!", Toast.LENGTH_SHORT).show()
                    openLoginPage()
                } else {
                    Toast.makeText(requireActivity(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openLoginPage() {
        parentFragmentManager.popBackStack()
    }
}