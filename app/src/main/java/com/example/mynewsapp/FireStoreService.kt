package com.example.mynewsapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreService {
    private val db: FirebaseFirestore = Firebase.firestore
    companion object {
        private const val TAG = "FireStoreAdapter"
    }

    fun savePreferences(collectionName: String, documentName: String, data: MutableMap<String, Any>) {
        db.collection(collectionName)
                .document("Preferences")
                .collection("All Preferences")
                .document(documentName)
                .set(data, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    fun getPreferences(collectionName: String, type: String): ArrayList<String> {

        val result = ArrayList<String>()

        db.collection(collectionName)
                .document("Preferences")
                .collection("All Preferences")
                .get()
                .addOnSuccessListener { documents ->
                    for (document: QueryDocumentSnapshot in documents!!) {
                        result.add(document.id)
                    }
                }
                .addOnFailureListener{
                    exception -> Log.w(TAG, "Error getting documents", exception)
                }

        return result
    }
}