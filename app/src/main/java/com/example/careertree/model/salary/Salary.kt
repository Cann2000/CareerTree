package com.example.careertree.model.salary

import com.google.gson.annotations.SerializedName

data class Salary(

    @SerializedName("personalSalaryCount")
    val personalSalaryCount:String?,

    @SerializedName("price")
    val price:String?

) {
}