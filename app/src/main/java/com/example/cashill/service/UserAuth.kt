package com.example.cashill.service

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.example.cashill.activity.HomeActivity
import com.example.cashill.activity.LoginActivity
import com.example.cashill.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserAuth(private val ctx: Context) : DataBase() {
    private val TAG = "UserAuth"
    private val sharedPref = "SHARED_PREF"
    private val uidKey = "UID_KEY"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    //creating user's account
    fun createAccount(user: User) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail: success")
                    user.id = auth.currentUser!!.uid
                    auth.signOut()
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d(TAG, "coroutineHasLaunched: forAddUserOnDatabase")
                        addUserOnDatabase(user)
                    }
                    goToLoginPage()
                } else {
                    Log.w(TAG, "createUserWithEmail: failed", it.exception)
                    Toast.makeText(
                        ctx, "Signup: failed", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //login to user's account
    fun loginAccount(email: String, password: String) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "loginUserWithEmail: success")
                    saveUserId(auth.currentUser!!.uid)
                    auth.signOut()
                    gotoHomePage()
                } else {
                    Log.w(TAG, "loginUserWithEmail: failed", it.exception)
                    Toast.makeText(
                        ctx, "Login: failed", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //signup has completed now goto login page
    private fun goToLoginPage() {
        Toast.makeText(
            ctx, "signed up successfully", Toast.LENGTH_SHORT
        ).show()
        ctx.startActivity(Intent(ctx, LoginActivity::class.java))
    }

    //login has completed now goto home page
    private fun gotoHomePage() {
        Toast.makeText(
            ctx, "login successfully", Toast.LENGTH_SHORT
        ).show()
        ctx.startActivity(Intent(ctx, HomeActivity::class.java))
    }

    //save user id in shared preferences
    private fun saveUserId(uid: String) {
        Log.d(TAG, "savingUserIdInSharedPreferences")
        sharedPreferences = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            this.putString(uidKey, uid)
            this.apply()
        }
    }

    //get user id from shared preferences
    fun getUserId(): String? {
        Log.d(TAG, "gettingUserIdFromSharedPreferences")
        sharedPreferences = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        return sharedPreferences.getString(uidKey, null)
    }

    //remove user id from share preferences
    fun deleteUserId() {
        Log.d(TAG, "deleteUserIdFromSharedPreferences")
        sharedPreferences = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            this.remove(uidKey)
            this.apply()
        }
    }
}