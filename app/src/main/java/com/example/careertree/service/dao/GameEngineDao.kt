package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.careertree.model.game.GameEngine

@Dao
interface GameEngineDao {

    @Insert
    suspend fun insertAll(vararg gameEngine: GameEngine):List<Long>

    @Query("DELETE FROM GameEngine")
    suspend fun deleteAll()

    @Query("SELECT * FROM GameEngine WHERE uuid = :gameEngineId")
    suspend fun getGameEngine(gameEngineId:Int): GameEngine

    @Query("SELECT * FROM GameEngine WHERE starred = :starred")
    suspend fun getStarredGameEngine(starred:Boolean): List<GameEngine>

    @Query("SELECT * FROM GameEngine")
    suspend fun getAllGameEngine():List<GameEngine>

    @Query("SELECT * From GameEngine Where compared = :compared")
    suspend fun getComparedGameEngine(compared:Boolean):List<GameEngine>

    @Update
    suspend fun updateGameEngine(gameEngine: GameEngine)
}