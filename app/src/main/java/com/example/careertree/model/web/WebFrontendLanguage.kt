package com.example.careertree.model.web

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WebFrontendLanguage(

    @ColumnInfo("Name")
    @SerializedName("name")
    val name:String?,

    @ColumnInfo("Information")
    @SerializedName("info")
    val info:String?,

    @ColumnInfo("VideoLink")
    @SerializedName("videoLink")
    val videoLink:String?,

    @ColumnInfo("ImageUrl")
    @SerializedName("imageUrl")
    val imageUrl:String?,

    ) {

    @PrimaryKey(autoGenerate = true)
    var uuid:Int = 0
}

