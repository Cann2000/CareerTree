package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.careertree.model.event.Event

@Dao
interface EventDao {

    @Insert
    suspend fun insertAll(vararg event: Event):List<Long>

    @Query("DELETE FROM event")
    suspend fun deleteAll()

    @Query("SELECT * FROM event WHERE uuid = :eventId")
    suspend fun getEvent(eventId:Int): Event

    @Query("SELECT * FROM event WHERE followedEvent = :follow")
    suspend fun getFollowedEvents(follow:Boolean): List<Event>

    @Query("SELECT * FROM event")
    suspend fun getAllEvent(): List<Event>

    @Update
    suspend fun updateEvent(event: Event)
}