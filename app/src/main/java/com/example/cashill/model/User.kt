package com.example.cashill.model

data class User(
    var id: String,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var password: String,
    val type: String,
    var records: Map<*, *>
) {
    //constructor for signup activity
    constructor(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        isLender: Boolean,
        isBorrower: Boolean,
    ) : this(
        "", name, email, phoneNumber, password,
        if (isLender) "lender" else if (isBorrower) "borrower" else "",
        mapOf("userStatus" to "clear")
    )

    //constructor for home activity
    constructor(data: Map<String, Any>) : this(
        data["id"].toString(),
        data["name"].toString(),
        data["email"].toString(),
        data["phoneNumber"].toString(),
        data["password"].toString(),
        data["type"].toString(),
        data["records"] as Map<*, *>
    )

    //check all field at the time of signup
    fun checkSignupFields(): Boolean {
        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()
            || password.isEmpty() || type.isEmpty()
        ) return false
        return true
    }
}

