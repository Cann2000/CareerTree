package com.example.careertree.model.homepage

import com.google.gson.annotations.SerializedName

class HomePageContent(

    @SerializedName("name")
    val contentName:String?,

    @SerializedName("imageUrl")
    val imageUrl:String?
) {

}