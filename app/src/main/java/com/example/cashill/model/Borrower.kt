package com.example.cashill.model

data class Borrower(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val loanAmount: Int,
    val tenure: Int
) {
    constructor(data: Map<String, Any?>) : this(
        data["id"].toString(),
        data["name"].toString(),
        data["phoneNumber"].toString(),
        data["loanAmount"].toString().toInt(),
        data["tenure"].toString().toInt(),
    )
}