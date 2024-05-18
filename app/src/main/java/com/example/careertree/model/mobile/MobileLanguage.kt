package com.example.careertree.model.mobile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class MobileLanguage(

    @ColumnInfo("Name")
    @SerializedName("name")
    val name: String?,

    @ColumnInfo("Information")
    @SerializedName("info")
    val info: String?,

    @ColumnInfo("Advantage")
    @SerializedName("advantage")
    val advantage:String?,

    @ColumnInfo("Disadvantage")
    @SerializedName("disadvantage")
    val disadvantage:String?,

    @ColumnInfo("WhyThisLanguage")
    @SerializedName("whyThisLanguage")
    val whyThisLanguage:String?,

    @ColumnInfo("Projects")
    @SerializedName("projects")
    val projects: String?,

    @ColumnInfo("Compared")
    var compared:Boolean,

    @ColumnInfo("Starred")
    var favorites:Boolean = false,

    @ColumnInfo("VideoLink")
    @SerializedName("videoLink")
    val videoLink:String?,

    @ColumnInfo("ImageUrl")
    @SerializedName("imageUrl")
    val imageUrl: String?,

    @ColumnInfo("Libraries")
    @SerializedName("library")
    val library: List<String>?


) {

    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = 0
}

