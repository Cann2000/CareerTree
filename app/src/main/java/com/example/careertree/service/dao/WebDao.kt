package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.careertree.model.web.WebBackendLanguage
import com.example.careertree.model.web.WebFrontendLanguage

@Dao
interface WebDao {

    // Backend
    @Insert
    suspend fun insertAllWebBackend(vararg language: WebBackendLanguage):List<Long>

    @Query("DELETE FROM WebBackendLanguage")
    suspend fun deleteAllWebBackend()

    @Query("SELECT * FROM WebBackendLanguage WHERE uuid = :webLanguageId")
    suspend fun getWebBackendLanguage(webLanguageId:Int): WebBackendLanguage

    @Query("SELECT * FROM WebBackendLanguage WHERE starred = :starred")
    suspend fun getStarredWebBackendLanguage(starred:Boolean): List<WebBackendLanguage>

    @Query("SELECT * FROM WebBackendLanguage")
    suspend fun getAllWebBackendLanguage(): List<WebBackendLanguage>

    @Query("SELECT * From WebBackendLanguage Where compared = :compared")
    suspend fun getComparedWebBackendLanguage(compared:Boolean):List<WebBackendLanguage>

    @Update
    suspend fun updateWebBackendLanguage(webBackendLanguage: WebBackendLanguage)

    // Frontend
    @Insert
    suspend fun insertAllWebFrontend(vararg language: WebFrontendLanguage):List<Long>

    @Query("DELETE FROM WebFrontendLanguage")
    suspend fun deleteAllWebFrontend()

    @Query("SELECT * FROM WebFrontendLanguage WHERE uuid = :webLanguageId")
    suspend fun getWebFrontendLanguage(webLanguageId:Int): WebFrontendLanguage

    @Query("SELECT * FROM webfrontendlanguage")
    suspend fun getAllWebFrontendLanguage(): List<WebFrontendLanguage>

}