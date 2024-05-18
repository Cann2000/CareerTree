package com.example.careertree.model.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Favorite(

    @ColumnInfo("Title")
    val languageName:String?,

    @ColumnInfo("ImageUrl")
    val imageUrl:String?

) {
    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = null

}