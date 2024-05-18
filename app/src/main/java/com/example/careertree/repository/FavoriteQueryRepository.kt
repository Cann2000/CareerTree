package com.example.careertree.repository

import com.example.careertree.model.favorite.Favorite
import com.example.careertree.service.dao.FavoriteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteQueryRepository@Inject constructor(private val favoritesDao: FavoriteDao) {

    suspend fun insertFavorite(favorite: Favorite) {

        CoroutineScope(Dispatchers.IO).launch {

            favoritesDao.insertFavorite(favorite)
        }
    }

    suspend fun deleteFavorites(title:String?) {

        CoroutineScope(Dispatchers.IO).launch {

            val favoriteList = favoritesDao.getAllFavorites()
            favoriteList.forEachIndexed { index, favorite ->

                if(favorite.languageName == title){
                    val uuid = favorite.uuid
                    favoritesDao.deleteFavorites(uuid!!)
                }
            }
        }
    }

    suspend fun getFavorites(uuid:Int): Favorite {

        return withContext(Dispatchers.IO){

            favoritesDao.getFavorite(uuid)
        }
    }

    suspend fun getAllFavorites():List<Favorite>{

        return withContext(Dispatchers.IO){

            favoritesDao.getAllFavorites()
        }
    }
}