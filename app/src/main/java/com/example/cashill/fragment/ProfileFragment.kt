package com.example.cashill.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cashill.activity.HomeActivity
import com.example.cashill.activity.MainActivity
import com.example.cashill.databinding.FragmentProfileBinding
import com.example.cashill.model.User


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var myActivity: HomeActivity
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        myActivity = activity as HomeActivity
        user = myActivity.user

        binding.profileName.text = user.name
        binding.profileEmail.text = user.email
        binding.profilePhone.text = user.phoneNumber

        binding.profileSignout.setOnClickListener {
            myActivity.auth.deleteUserId()
            startActivity(Intent(myActivity, MainActivity::class.java))
            myActivity.finish()
        }

        return binding.root
    }

}