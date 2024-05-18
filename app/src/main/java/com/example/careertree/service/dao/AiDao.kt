package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.careertree.model.ai.AiLanguage

@Dao
interface AiDao {

    @Insert
    suspend fun insertAll(vararg aiLanguage: AiLanguage):List<Long>

    @Query("DELETE FROM AiLanguage")
    suspend fun deleteAll()

    @Query("SELECT * FROM AiLanguage WHERE uuid = :aiLanguageId")
    suspend fun getAiLanguage(aiLanguageId:Int): AiLanguage

    @Query("SELECT * FROM AiLanguage WHERE starred = :starred")
    suspend fun getStarredAiLanguage(starred:Boolean): List<AiLanguage>

    @Query("SELECT * FROM AiLanguage")
    suspend fun getAllAiLanguage():List<AiLanguage>

    @Query("SELECT * From AiLanguage Where compared = :compared")
    suspend fun getComparedAiLanguage(compared:Boolean):List<AiLanguage>

    @Update
    suspend fun updateAiLanguage(aiLanguage: AiLanguage)
}