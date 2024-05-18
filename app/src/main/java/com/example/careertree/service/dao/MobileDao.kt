package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.careertree.model.mobile.MobileLanguage

@Dao
interface MobileDao {

    @Insert
    suspend fun insertAll(vararg mobileLanguage: MobileLanguage):List<Long>

    @Query("DELETE FROM MobileLanguage")
    suspend fun deleteAll()

    @Query("SELECT * FROM MobileLanguage WHERE uuid = :mobileLanguageId")
    suspend fun getMobileLanguage(mobileLanguageId:Int): MobileLanguage

    @Query("SELECT * FROM MobileLanguage WHERE starred = :starred")
    suspend fun getStarredMobileLanguage(starred:Boolean): List<MobileLanguage>

    @Query("SELECT * FROM MobileLanguage")
    suspend fun getAllMobileLanguage():List<MobileLanguage>

    @Query("SELECT * From MobileLanguage Where compared = :compared")
    suspend fun getComparedMobileLanguage(compared:Boolean):List<MobileLanguage>

    @Update
    suspend fun updateMobileLanguage(mobileLanguage: MobileLanguage)

}