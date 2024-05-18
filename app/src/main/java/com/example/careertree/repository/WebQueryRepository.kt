package com.example.careertree.repository

import android.view.View
import androidx.navigation.Navigation
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.model.web.WebBackendLanguage
import com.example.careertree.model.web.WebFrontendLanguage
import com.example.careertree.service.dao.WebDao
import com.example.careertree.view.web.WebBackendContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WebQueryRepository @Inject constructor(
    private val webDao: WebDao,
    private val favoriteQueryRepository: FavoriteQueryRepository) {

    suspend fun insertAllWebBackend(languageList :List<WebBackendLanguage>){

        CoroutineScope(Dispatchers.IO).launch {

            webDao.deleteAllWebBackend()

            val uuid = webDao.insertAllWebBackend(*languageList.toTypedArray())
            languageList.forEachIndexed { index, webBackendLanguage ->
                webBackendLanguage.uuid = uuid[index].toInt()
            }
        }
    }

    suspend fun insertAllWebFrontend(languageList :List<WebFrontendLanguage>){

        CoroutineScope(Dispatchers.IO).launch {

            try {

                webDao.deleteAllWebFrontend()

                val uuid = webDao.insertAllWebFrontend(*languageList.toTypedArray())
                languageList.forEachIndexed { index, webFrontendLanguage ->
                    webFrontendLanguage.uuid = uuid[index].toInt()
                }
            }
            catch (e:Exception){
                println(e.printStackTrace())
            }
        }
    }

    suspend fun getWebBackendLanguage(uuid:Int):WebBackendLanguage{

        return withContext(Dispatchers.IO){
            webDao.getWebBackendLanguage(uuid)
        }
    }
    suspend fun getWebFrontendLanguage(uuid:Int):WebFrontendLanguage{

        return withContext(Dispatchers.IO){
            webDao.getWebFrontendLanguage(uuid)
        }
    }

    suspend fun getAllWebBackendLanguage():List<WebBackendLanguage>{

        return withContext(Dispatchers.IO){
            webDao.getAllWebBackendLanguage()
        }
    }
    suspend fun getAllWebFrontendLanguage():List<WebFrontendLanguage>{

        return withContext(Dispatchers.IO){
            webDao.getAllWebFrontendLanguage()
        }
    }

    suspend fun getComparedWebBackendLanguage(compared:Boolean):List<WebBackendLanguage>{

        return withContext(Dispatchers.IO){

            webDao.getComparedWebBackendLanguage(compared)
        }
    }

    suspend fun updateWebBackendLanguageToCompare(webBackendLanguage: WebBackendLanguage,view:View){

        CoroutineScope(Dispatchers.IO).launch {

            if(!webBackendLanguage.compared){

                webBackendLanguage.compared = true
                webDao.updateWebBackendLanguage(webBackendLanguage)

                val comparedWebBackendLanguage =webDao.getComparedWebBackendLanguage(true)

                if(comparedWebBackendLanguage.size == 2){

                    withContext(Dispatchers.Main){

                        delay(100)
                        val action = WebBackendContentFragmentDirections.actionBackendContentFragmentToCompareFragment("web")
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
            else{
                webBackendLanguage.compared = false
                webDao.updateWebBackendLanguage(webBackendLanguage)
            }
        }
    }
    suspend fun updateWebBackendToStar(webBackendLanguage: WebBackendLanguage?){

        CoroutineScope(Dispatchers.IO).launch {

            webBackendLanguage?.let {

                if(!webBackendLanguage.favorites){

                    webBackendLanguage.favorites = true
                    webDao.updateWebBackendLanguage(webBackendLanguage)

                    val favorite = Favorite(webBackendLanguage.name,webBackendLanguage.imageUrl)
                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{

                    webBackendLanguage.favorites = false
                    webDao.updateWebBackendLanguage(webBackendLanguage)

                    favoriteQueryRepository.deleteFavorites(webBackendLanguage.name)
                }
            }
        }
    }
    suspend fun makeComparedFalse(comparedWebBackendLanguage:List<WebBackendLanguage>){

        CoroutineScope(Dispatchers.IO).launch{

            comparedWebBackendLanguage[0].compared = false
            comparedWebBackendLanguage[1].compared = false

            webDao.updateWebBackendLanguage(comparedWebBackendLanguage[0])
            webDao.updateWebBackendLanguage(comparedWebBackendLanguage[1])
        }
    }
}