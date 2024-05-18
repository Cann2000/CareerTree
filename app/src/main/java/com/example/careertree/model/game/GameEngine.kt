package com.example.careertree.model.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GameEngine(

    @ColumnInfo("Name")
    @SerializedName("name")
    val name:String,

    @ColumnInfo("Information")
    @SerializedName("info")
    val info:String,

    @ColumnInfo("Advantage")
    @SerializedName("advantage")
    val advantage:String,

    @ColumnInfo("Disadvantage")
    @SerializedName("disadvantage")
    val disadvantage:String,

    @ColumnInfo("Features")
    @SerializedName("features")
    val features:String,

    @ColumnInfo("Compared")
    var compared:Boolean,

    @ColumnInfo("Starred")
    var favorites:Boolean = false,

    @ColumnInfo("VideoLink")
    @SerializedName("videoLink")
    val videoLink:String?,

    @ColumnInfo("ImageUrl")
    @SerializedName("imageUrl")
    val imageUrl:String,

    @ColumnInfo("Projects")
    @SerializedName("projects")
    val projects:List<String>
) {

    @PrimaryKey(autoGenerate = true)
    var uuid: Int? = 0
}