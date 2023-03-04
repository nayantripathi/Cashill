package com.example.cashill.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.cashill.R
import com.example.cashill.databinding.ActivityHomeBinding
import com.example.cashill.fragment.BorrowerFragment
import com.example.cashill.fragment.LenderFragment
import com.example.cashill.fragment.ProfileFragment
import com.example.cashill.fragment.RequestFragment
import com.example.cashill.model.User
import com.example.cashill.service.UserAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    private lateinit var binding: ActivityHomeBinding
    lateinit var auth: UserAuth
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = UserAuth(this)

        //launch coroutine for get user data from database
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "coroutineHasLaunched: forGetUserFromDatabase")
            val uid = auth.getUserId()!!
            val task = auth.getUserFromDatabase(uid)
            task.await()

            //convert JSON data into user object
            user = User(task.result.data!!.toMap())
            openUserFragment()
        }

        //open user profile
        binding.homeProfileButton.setOnClickListener {
            setFragment(ProfileFragment())
        }
    }

    private fun openUserFragment() {
        binding.homeProgressBar.visibility = View.INVISIBLE
        when (user.type) {
            "borrower" -> {
                if (user.records["userStatus"] == "clear") {
                    Log.d(TAG, "openRequestFragment")
                    setFragment(RequestFragment())
                } else {
                    Log.d(TAG, "openBorrowerFragment")
                    setFragment(BorrowerFragment())
                }

            }
            "lender" -> {
                Log.d(TAG, "openLenderFragment")
                setFragment(LenderFragment())
            }
            else -> {
                Log.e(TAG, "userTypeIsNotSpecify")
                Toast.makeText(
                    this, "userTypeIsNotAvailable", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.homeFrameLayout, fragment)
            setReorderingAllowed(true)
        }
    }
}
