package com.example.mynewsapp.services

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * This service interacts with our firebase cloud storage and allows us to store and retrieve
 * information.
 * @author Seabstian Gappa
 */
class FireStoreService {
    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val TAG = "FireStoreService"
    }

    /**
     * Save a preference to a users account in (preference name, preference type) format.
     */
    fun savePreferences(collectionName: String, data: MutableMap<String, Any>) {
        db.collection(collectionName)
                .document("Preferences")
                .set(data, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    /**
     * Retrieves all preferences.
     */
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

    /**
     * Remove a preference.
     */
    fun removePreference(collectionName: String, field: String) {
        val updates = hashMapOf<String, Any>(
                field to FieldValue.delete()
        )

        db.collection(collectionName)
                .document("Preferences")
                .update(updates)
                .addOnSuccessListener { Log.d(TAG, "Field successfully deleted!") }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "delete failed with ", exception)
                }
    }

    /**
     * Save article source preference in (articleName, articleSource) format.
     */
    fun saveArticleBySource(collectionName: String, data: MutableMap<String, Any>) {
        db.collection(collectionName)
            .document("Articles")
            .collection("Source")
            .document("Saved")
            .set(data, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    /**
     * Save article country preference in (articleName, articleCountry) format.
     */
    fun saveArticleByCountry(collectionName: String, data: MutableMap<String, Any>) {
        db.collection(collectionName)
            .document("Articles")
            .collection("Country")
            .document("Saved")
            .set(data, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    /**
     * Save article topic preference in (articleName, articleTopic) format.
     */
    fun saveArticleByTopic(collectionName: String, data: MutableMap<String, Any>) {
        db.collection(collectionName)
            .document("Articles")
            .collection("Topic")
            .document("Saved")
            .set(data, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    /**
     * Get all saved articles by source.
     */
    fun getArticlesBySource(collectionName: String): Task<DocumentSnapshot> {
        return db.collection(collectionName)
            .document("Articles")
            .collection("Source")
            .document("Saved")
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

    /**
     * Get all saved articles by country.
     */
    fun getArticlesByCountry(collectionName: String): Task<DocumentSnapshot> {
        return db.collection(collectionName)
            .document("Articles")
            .collection("Country")
            .document("Saved")
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

    /**
     * Get all saved articles by topic.
     */
    fun getArticlesByTopic(collectionName: String): Task<DocumentSnapshot> {
        return db.collection(collectionName)
            .document("Articles")
            .collection("Topic")
            .document("Saved")
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