package com.example.cashill.service

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.cashill.R
import com.example.cashill.model.Borrower
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class BorrowersAdapter(
    context: Context,
    list: List<Borrower>,
    private val uid: String
) : ArrayAdapter<Borrower>(context, 0, list) {
    val db = DataBase()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currentItem = getItem(position)!!
        val currItemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.view_borrower, parent, false)

        currItemView.findViewById<TextView>(R.id.viewBorrowerName)
            .text = currentItem.name
        currItemView.findViewById<TextView>(R.id.viewBorrowerAmount)
            .text = buildString {
            append("Loan Amount: ")
            append(currentItem.loanAmount)
        }
        currItemView.findViewById<TextView>(R.id.viewBorrowerTenure)
            .text = buildString {
            append("Loan Tenure: ")
            append(currentItem.tenure)
        }

        val approveButton = currItemView.findViewById<Button>(R.id.viewBorrowerApproveButton)
        approveButton.setOnClickListener {
            approveButton.isEnabled = false
            CoroutineScope(Dispatchers.IO).launch {
                db.updateUserRecord(
                    uid,
                    mapOf(
                        "userStatus" to "given",
                        "borrowerId" to currentItem.id,
                        "loanAmount" to currentItem.loanAmount,
                        "loanTenure" to currentItem.tenure
                    )
                ).await()
                db.updateUserRecord(
                    currentItem.id,
                    mapOf(
                        "userStatus" to "confirm",
                        "lenderId" to uid,
                        "loanAmount" to currentItem.loanAmount,
                        "tenure" to currentItem.tenure
                    )
                ).await()
                db.removeUserFromListOfPendingLoans(currentItem.id)
            }
        }
        return currItemView
    }
}