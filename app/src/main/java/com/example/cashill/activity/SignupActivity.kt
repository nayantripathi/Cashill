package com.example.cashill.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cashill.service.UserAuth
import com.example.cashill.databinding.ActivitySignupBinding
import com.example.cashill.model.User

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //go back to MainActivity
        binding.signupBack.setOnClickListener {
            finish()
        }

        //start creating user's account
        binding.signupButton.setOnClickListener {
            val user = User(
                binding.signupName.text.toString(),
                binding.signupEmail.text.toString(),
                binding.signupPhone.text.toString(),
                binding.signupPassword.text.toString(),
                binding.signupLender.isChecked,
                binding.signupBorrower.isChecked
            )

            if (user.checkSignupFields()) {
                binding.signupButton.isEnabled = false
                binding.signupProgressBar.visibility = View.VISIBLE

                //create user account
                UserAuth(this).createAccount(user)
            } else {
                Toast.makeText(
                    this, "Please fill all details", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}