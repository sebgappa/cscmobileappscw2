package com.example.mynewsapp.services

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreService {
    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val TAG = "FireStoreAdapter"
    }

    fun savePreferences(collectionName: String, data: MutableMap<String, Any>) {
        db.collection(collectionName)
                .document("Preferences")
                .set(data, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    fun getPreferences(collectionName: String): Task<DocumentSnapshot> {

        return db.collection(collectionName)
                .document("Preferences")
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
    }
}