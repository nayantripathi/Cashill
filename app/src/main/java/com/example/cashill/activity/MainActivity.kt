package com.example.cashill.activity

/*
    Nayan Tripathi
    Feb 20, 2023
    Lucknow
 */


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cashill.service.UserAuth
import com.example.cashill.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //goto login activity
        binding.welcomeLoginButton.setOnClickListener {
            Log.d(TAG, "gotoLoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //goto signup activity
        binding.welcomeSignupButton.setOnClickListener {
            Log.d(TAG, "gotoSignupActivity")
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        //check user is already login
        val uid = UserAuth(this).getUserId()
        if (uid != null) {
            Log.d(TAG, "userIsAlreadyLogin: gotoHomeActivity")
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}