package com.example.cashill.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.cashill.R
import com.example.cashill.activity.HomeActivity
import com.example.cashill.databinding.FragmentRequestBinding
import com.example.cashill.model.Borrower
import com.example.cashill.service.DataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RequestFragment : Fragment() {
    private val db = DataBase()
    private lateinit var binding: FragmentRequestBinding
    private lateinit var myActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestBinding.inflate(layoutInflater)
        myActivity = activity as HomeActivity

        binding.borrowerCreateRequestButton.setOnClickListener {
            binding.borrowerCreateRequestButton.visibility = View.GONE
            binding.borrowerName1.text = myActivity.user.name
            binding.borrowerRequestPageLayout.visibility = View.VISIBLE
        }

        binding.borrowerRequestButton.setOnClickListener {
            val amount = binding.borrowerInputAmount.text.toString().toInt()
            val tenure = binding.borrowerInputTenure.text.toString().toInt()

            if (amount in 10..1000 && tenure in 1..12) {
                binding.borrowerRequestButton.isEnabled = false
                val records = mapOf<String, Any>(
                    "userStatus" to "pending",
                    "loanAmount" to amount,
                    "tenure" to tenure
                )
                CoroutineScope(Dispatchers.IO).launch {
                    var task = db.updateUserRecord(myActivity.user.id, records)
                    task.await()
                    myActivity.user.records = records
                    val borrower = Borrower(
                        myActivity.user.id,
                        myActivity.user.name,
                        myActivity.user.phoneNumber,
                        amount,
                        tenure
                    )
                    task = db.addUserToListOfPendingLoans(borrower)
                    task.await()

                    myActivity.supportFragmentManager.commit {
                        replace<BorrowerFragment>(R.id.homeFrameLayout)
                        setReorderingAllowed(true)
                    }
                }

            } else {
                Toast.makeText(
                    activity, "Enter valid inputs", Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }
}