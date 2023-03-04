package com.example.cashill.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cashill.service.UserAuth
import com.example.cashill.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //go back to MainActivity
        binding.loginBack.setOnClickListener {
            finish()
        }

        //start login to user's account
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                binding.loginButton.isEnabled = false
                binding.loginProgressBar.visibility = View.VISIBLE

                //login user account
                UserAuth(this).loginAccount(email, password)
            } else {
                Toast.makeText(
                    this, "email & password can't be empty", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}