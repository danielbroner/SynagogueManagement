package com.synagoguemanagement.synagoguemanagement.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

object AuthManager {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signUpUser(email: String, password: String): Task<AuthResult> {
        val createUserWithEmailAndPassword = auth.createUserWithEmailAndPassword(email, password)
        return createUserWithEmailAndPassword
    }

    fun signInUser(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun getUser(): FirebaseUser {
        return auth.currentUser!!
    }

    fun isAdmin(): Boolean {
        return "shay@test.com" == getUser().email
    }
}
