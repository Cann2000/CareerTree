package com.example.careertree.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.careertree.model.favorite.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite where uuid= :favoriteId")
    suspend fun deleteFavorites(favoriteId:Int)

    @Query("SELECT * FROM favorite WHERE uuid = :favoriteId")
    suspend fun getFavorite(favoriteId:Int): Favorite

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorites(): List<Favorite>
}