package com.synagoguemanagement.synagoguemanagement.db

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.synagoguemanagement.synagoguemanagement.auth.AuthManager
import com.synagoguemanagement.synagoguemanagement.data.ReservedSeats
import com.synagoguemanagement.synagoguemanagement.data.Seat

class DbManager {

    private val db = Firebase.firestore
    private val collectionName = "reserved_seats"

    companion object {
        private var instance: DbManager? = null
        fun getInstance(): DbManager? {
            if (instance == null) {
                instance = DbManager()
                return instance
            }
            return instance
        }
    }

    fun reserveSeats(updatedSeats: ReservedSeats) {
        val docRef = db.collection(collectionName).document(updatedSeats.date)

        docRef.set(updatedSeats, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Document created/updated successfully!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun getReservedSeats(date: String, onSuccess: (DocumentSnapshot) -> Unit) {
        val docRef = db.collection(collectionName).document(date)

        docRef.get().addOnSuccessListener(onSuccess)
            .addOnFailureListener { exception ->
                println("Error getting document: ${exception.message}")
            }
    }

    fun removeReservation(date: String) {
        getReservedSeats(date) { document ->
            if (document.exists()) {
                val beforeRemoval = document.toObject(ReservedSeats::class.java)!!
                val updated = ReservedSeats(date, beforeRemoval.seats.filter {
                    seat: Seat -> seat.userId != AuthManager.getCurrentUser().uid
                })

                if (updated.seats.isEmpty()) deleteDocument(date) else reserveSeats(updated)
            }
        }
    }

    private fun deleteDocument(date: String) {
        db.collection(collectionName).document(date).delete()
    }
}