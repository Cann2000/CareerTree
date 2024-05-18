package com.example.careertree.model.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Event(

    @ColumnInfo("EventName")
    @SerializedName("name")
    val name:String?,

    @ColumnInfo("EventInfo")
    @SerializedName("info")
    val info:String?,

    @ColumnInfo("ApplicationDeadline")
    @SerializedName("application_deadline")
    val applicationDeadline:String?,

    @ColumnInfo("FollowedEvent")
    var followedEvent:Boolean,

    @ColumnInfo("EventLink")
    @SerializedName("link")
    val link:String?,

    @ColumnInfo("ImageUrl")
    @SerializedName("imageUrl")
    val imageUrl:String?


) {
    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = 0
}