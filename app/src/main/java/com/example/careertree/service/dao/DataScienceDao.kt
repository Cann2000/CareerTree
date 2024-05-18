package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.careertree.model.data_science.DataScienceLanguage

@Dao
interface DataScienceDao {

    @Insert
    suspend fun insertAll(vararg dataScienceLanguage: DataScienceLanguage):List<Long>

    @Query("DELETE FROM DataScienceLanguage")
    suspend fun deleteAll()

    @Query("SELECT * FROM DataScienceLanguage WHERE uuid = :dataScienceLanguageId")
    suspend fun getDataScienceLanguage(dataScienceLanguageId:Int): DataScienceLanguage

    @Query("SELECT * FROM DataScienceLanguage WHERE starred = :starred")
    suspend fun getStarredDataScienceLanguage(starred:Boolean): List<DataScienceLanguage>

    @Query("SELECT * FROM DataScienceLanguage")
    suspend fun getAllDataScienceLanguage():List<DataScienceLanguage>

    @Update
    suspend fun updateDataScienceLanguage(dataScienceLanguage: DataScienceLanguage)
}