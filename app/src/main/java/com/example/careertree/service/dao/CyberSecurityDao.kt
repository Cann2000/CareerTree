package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.careertree.model.cyber_security.CyberSecurityLanguage

@Dao
interface CyberSecurityDao {

    @Insert
    suspend fun insertAll(vararg cyberSecurityLanguage: CyberSecurityLanguage):List<Long>

    @Query("DELETE FROM CyberSecurityLanguage")
    suspend fun deleteAll()

    @Query("SELECT * FROM CyberSecurityLanguage WHERE uuid = :cyberSecurityLanguageId")
    suspend fun getCyberSecurityLanguage(cyberSecurityLanguageId:Int): CyberSecurityLanguage

    @Query("SELECT * FROM CyberSecurityLanguage WHERE starred = :starred")
    suspend fun getStarredCyberSecurityLanguage(starred:Boolean): List<CyberSecurityLanguage>

    @Query("SELECT * FROM CyberSecurityLanguage")
    suspend fun getAllCyberSecurityLanguage():List<CyberSecurityLanguage>

    @Query("SELECT * From CyberSecurityLanguage Where compared = :compared")
    suspend fun getComparedCyberSecurityLanguage(compared:Boolean):List<CyberSecurityLanguage>

    @Update
    suspend fun updateCyberSecurityLanguage(cyberSecurityLanguage: CyberSecurityLanguage)
}