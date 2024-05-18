package com.example.careertree.repository

import android.view.View
import androidx.navigation.Navigation
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.model.ai.AiLanguage
import com.example.careertree.service.dao.AiDao
import com.example.careertree.view.ai.AiContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AiQueryRepository@Inject constructor(private val aiDao: AiDao,private val favoriteQueryRepository: FavoriteQueryRepository){

    suspend fun insertAllAiLanguage(aiLanguageList: List<AiLanguage>){

        CoroutineScope(Dispatchers.IO).launch {

            aiDao.deleteAll()

            val uuid = aiDao.insertAll(*aiLanguageList.toTypedArray())
            aiLanguageList.forEachIndexed { index, aiLanguage ->

                aiLanguage.uuid = uuid[index].toInt()
            }
        }
    }
    suspend fun getAllAiLanguage():List<AiLanguage>{

        return withContext(Dispatchers.IO){

            aiDao.getAllAiLanguage()
        }
    }
    suspend fun getAiLanguage(uuid:Int):AiLanguage{

        return withContext(Dispatchers.IO){

            aiDao.getAiLanguage(uuid)
        }
    }
    suspend fun getComparedAiLanguage(compared:Boolean):List<AiLanguage>{

        return withContext(Dispatchers.IO){
            aiDao.getComparedAiLanguage(compared)
        }
    }
    suspend fun updateAiLanguageToCompare(aiLanguage: AiLanguage, view: View){

        CoroutineScope(Dispatchers.IO).launch {

            if(!aiLanguage.compared){

                aiLanguage.compared = true
                aiDao.updateAiLanguage(aiLanguage) // Compared yaptıktan sonra güncelle

                val comparedAiLanguage = aiDao.getComparedAiLanguage(true)

                if(comparedAiLanguage.size == 2){

                    withContext(Dispatchers.Main){
                        delay(100)
                        val action = AiContentFragmentDirections.actionAiContentFragmentToCompareFragment("ai")
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
            else{

                aiLanguage.compared = false
                aiDao.updateAiLanguage(aiLanguage) // Compared yaptıktan sonra güncelle
            }
        }

    }

    suspend fun updateAiLanguageToStar(aiLanguage: AiLanguage?){

        CoroutineScope(Dispatchers.IO).launch {

            aiLanguage?.let {

                if(!aiLanguage.favorites){

                    aiLanguage.favorites = true
                    aiDao.updateAiLanguage(aiLanguage)

                    val favorite = Favorite(aiLanguage.name,aiLanguage.imageUrl)
                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{

                    aiLanguage.favorites = false
                    aiDao.updateAiLanguage(aiLanguage)

                    favoriteQueryRepository.deleteFavorites(aiLanguage.name)
                }
            }
        }
    }
    suspend fun makeComparedFalse(comparedAiLanguage:List<AiLanguage>){

        CoroutineScope(Dispatchers.IO).launch{

            comparedAiLanguage[0].compared = false
            comparedAiLanguage[1].compared = false

            aiDao.updateAiLanguage(comparedAiLanguage[0])
            aiDao.updateAiLanguage(comparedAiLanguage[1])
        }
    }
}