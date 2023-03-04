package com.example.cashill.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cashill.activity.HomeActivity
import com.example.cashill.databinding.FragmentLenderBinding
import com.example.cashill.model.Borrower
import com.example.cashill.service.BorrowersAdapter
import com.example.cashill.service.DataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//const val TAG = "LenderFragment"

class LenderFragment : Fragment() {
    private val db = DataBase()
    private lateinit var binding: FragmentLenderBinding
    private var list = mutableListOf<Borrower>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLenderBinding.inflate(layoutInflater)
        CoroutineScope(Dispatchers.IO).launch {
            val task = db.getListOfPendingLoans()
            task.await()
            for (document in task.result.documents) {
                list.add(Borrower(document.data!!.toMap()))
            }
            setList()
        }

        return binding.root
    }

    private fun setList() {
        CoroutineScope(Dispatchers.Main).launch {
            val arrayAdapter = BorrowersAdapter(
                activity as HomeActivity,
                list,
                (activity as HomeActivity).user.id
            )
            binding.lenderListView.adapter = arrayAdapter
        }
    }

}