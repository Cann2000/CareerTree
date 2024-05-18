package com.example.careertree.model.ai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class AiLanguage(

    @ColumnInfo("Name")
    @SerializedName("name")
    val name:String?,

    @ColumnInfo("Information")
    @SerializedName("info")
    val info:String?,

    @ColumnInfo("WhyThisLanguage")
    @SerializedName("whyThisLanguage")
    val whyThisLanguage:String?,

    @ColumnInfo("Advantage")
    @SerializedName("advantage")
    val advantage:String?,

    @ColumnInfo("Disadvantage")
    @SerializedName("disadvantage")
    val disadvantage:String?,

    @ColumnInfo("Compared")
    var compared:Boolean,

    @ColumnInfo("Starred")
    var favorites:Boolean = false,

    @ColumnInfo("PossibleApplications")
    @SerializedName("possible_applications")
    val possibleApplications:String?,

    @ColumnInfo("VideoLink")
    @SerializedName("videoLink")
    val videoLink:String?,

    @ColumnInfo("ImageUrl")
    @SerializedName("imageUrl")
    val imageUrl: String?

) {

    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = 0
}