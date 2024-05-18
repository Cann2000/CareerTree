package com.example.careertree.model.blog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Blog(

    @ColumnInfo("ImageUrl")
    @SerializedName("imageUrl")
    val imageUrl:String?,

    @ColumnInfo("Link")
    @SerializedName("link")
    val link:String?

) {
    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = 0
}