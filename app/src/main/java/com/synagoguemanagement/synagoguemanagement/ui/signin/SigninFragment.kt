package com.synagoguemanagement.synagoguemanagement.ui.signin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.synagoguemanagement.synagoguemanagement.R

class SigninFragment : Fragment(R.layout.fragment_signin) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailInput = view.findViewById<EditText>(R.id.emailEditText)
        val passwordInput = view.findViewById<EditText>(R.id.passwordEditText)
        val signInButton = view.findViewById<Button>(R.id.signInButton)

        signInButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Navigate to HomeFragment after successful login
                //findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                //TODO
            } else {
                Toast.makeText(requireContext(), "Enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}