package com.example.cashill.service

import android.util.Log
import com.example.cashill.model.Borrower
import com.example.cashill.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class DataBase {
    private val TAG = "UserDatabase"
    private val db = Firebase.firestore

    //add user on database
    protected fun addUserOnDatabase(user: User) {
        user.password = "*****"
        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "addUserOnDatabase: success")
                } else {
                    Log.w(TAG, "addUserOnDatabase: failed", it.exception)
                    //updateUserData() or deleteSignupProfile() ...
                }
            }
    }

    //get user from database
    fun getUserFromDatabase(uid: String): Task<DocumentSnapshot> {

        return db.collection("users")
            .document(uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "getUserFromDatabase: success")
                } else {
                    Log.w(TAG, "getUserFromDatabase: failed", it.exception)
                }
            }
    }

    //update user in data base
    fun updateUserRecord(uid: String, record: Map<String, Any>): Task<Void> {

        return db.collection("users")
            .document(uid)
            .update("records", record)
            .addOnSuccessListener {
                Log.d(TAG, "updateUserRecords: success")
            }
            .addOnFailureListener {
                Log.w(TAG, "updateUserRecords: failed")
            }
    }

    //add user in list of pending loans
    fun addUserToListOfPendingLoans(borrower: Borrower): Task<Void> {
        return db.collection("pendingLoans")
            .document(borrower.id)
            .set(borrower)
            .addOnSuccessListener {
                Log.d(TAG, "addUserToListOfPendingLoans: success")
            }
            .addOnFailureListener {
                Log.d(TAG, "addUserToListOfPendingLoans: failed")
            }
    }

    //remove user from list of pending loans
    fun removeUserFromListOfPendingLoans(uid: String): Task<Void> {
        return db.collection("pendingLoans")
            .document(uid)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "deleteUserFromListOfPendingLoans: success")
            }
            .addOnFailureListener {
                Log.d(TAG, "deleteUserFromListOfPendingLoans: failed")
            }
    }

    //get list of pending loans from database
    fun getListOfPendingLoans(): Task<QuerySnapshot> {
        return db.collection("pendingLoans")
            .get()
    }
}