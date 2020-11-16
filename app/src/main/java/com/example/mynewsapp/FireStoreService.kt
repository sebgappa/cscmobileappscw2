package com.example.mynewsapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreService {
    private val db: FirebaseFirestore = Firebase.firestore
    companion object {
        private const val TAG = "FireStoreAdapter"
    }

    fun Save(collectionName: String, documentName: String, data: MutableMap<String, Any>) {
        db.collection(collectionName)
                .document(documentName)
                .set(data)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    fun Get(collectionName: String, documentName: String): MutableMap<String, Any>? {

        var data: MutableMap<String, Any>? = null

        db.collection(collectionName)
                .document(documentName)
                .get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, "${result.id} => ${result.data}")
                    data = result.data
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

        return data
    }
}