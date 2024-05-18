package com.example.careertree.repository

import com.example.careertree.model.data_science.DataScienceLanguage
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.service.dao.DataScienceDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataScienceQueryRepository@Inject constructor(private val dataScienceDao: DataScienceDao, private val favoriteQueryRepository: FavoriteQueryRepository) {

    suspend fun insertAllDataScienceLanguage(dataScienceLanguage: List<DataScienceLanguage>){

        CoroutineScope(Dispatchers.IO).launch {

            dataScienceDao.deleteAll()

            val uuid = dataScienceDao.insertAll(*dataScienceLanguage.toTypedArray())
            dataScienceLanguage.forEachIndexed { index, dataScienceLanguage ->

                dataScienceLanguage.uuid = uuid[index].toInt()
            }
        }
    }
    suspend fun getAllDataScienceLanguage():List<DataScienceLanguage>{

        return withContext(Dispatchers.IO){

            dataScienceDao.getAllDataScienceLanguage()
        }
    }
    suspend fun getDataScienceLanguage(uuid:Int): DataScienceLanguage {

        return withContext(Dispatchers.IO){

            dataScienceDao.getDataScienceLanguage(uuid)
        }
    }

    suspend fun updateDataScienceLanguageToStar(dataScienceLanguage: DataScienceLanguage?){

        CoroutineScope(Dispatchers.IO).launch {

            dataScienceLanguage?.let {

                if(!dataScienceLanguage.favorites){

                    dataScienceLanguage.favorites = true
                    dataScienceDao.updateDataScienceLanguage(dataScienceLanguage)

                    val favorite = Favorite(dataScienceLanguage.name,dataScienceLanguage.imageUrl)
                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{

                    dataScienceLanguage.favorites = false
                    dataScienceDao.updateDataScienceLanguage(dataScienceLanguage)

                    favoriteQueryRepository.deleteFavorites(dataScienceLanguage.name)
                }
            }
        }
    }

}