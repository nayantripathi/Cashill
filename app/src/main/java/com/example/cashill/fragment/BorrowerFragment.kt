package com.example.cashill.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.cashill.R
import com.example.cashill.activity.HomeActivity
import com.example.cashill.databinding.FragmentBorrowerBinding
import com.example.cashill.service.DataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BorrowerFragment : Fragment() {
    private val db = DataBase()
    private lateinit var binding: FragmentBorrowerBinding
    private lateinit var myActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBorrowerBinding.inflate((layoutInflater))
        myActivity = activity as HomeActivity

        binding.borrowerName.text = myActivity.user.name
        binding.borrowerStatus.text = buildString {
            append("Your status: ")
            append(myActivity.user.records["userStatus"])
        }
        binding.borrowerLoanAmount.text = buildString {
            append("Loan amount: ")
            append(myActivity.user.records["loanAmount"])
        }

        if (myActivity.user.records["userStatus"] == "confirm") {
            binding.borrowerCancelButton.visibility = View.GONE
            binding.borrowerReturnButton.visibility = View.VISIBLE
        }

        binding.borrowerCancelButton.setOnClickListener {
            val records = mapOf<String, Any>(
                "userStatus" to "clear",
            )
            CoroutineScope(Dispatchers.IO).launch {
                val uid = myActivity.user.id
                db.removeUserFromListOfPendingLoans(uid).await()
                db.updateUserRecord(uid, records).await()
                myActivity.user.records = records
                myActivity.supportFragmentManager.commit {
                    replace<RequestFragment>(R.id.homeFrameLayout)
                    setReorderingAllowed(true)
                }
            }
        }
        binding.borrowerReturnButton.setOnClickListener {
            val records = mapOf<String, Any>(
                "userStatus" to "clear",
            )
            CoroutineScope(Dispatchers.IO).launch {
                val uid = myActivity.user.id
                val lenderId = myActivity.user.records["lenderId"].toString()
                db.updateUserRecord(lenderId, records).await()
                db.updateUserRecord(uid, records).await()
                myActivity.user.records = records
                myActivity.supportFragmentManager.commit {
                    replace<RequestFragment>(R.id.homeFrameLayout)
                    setReorderingAllowed(true)
                }
            }
        }

        return binding.root
    }


}