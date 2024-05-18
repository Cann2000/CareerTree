package com.example.careertree.repository

import android.view.View
import androidx.navigation.Navigation
import com.example.careertree.model.cyber_security.CyberSecurityLanguage
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.service.dao.CyberSecurityDao
import com.example.careertree.view.cyber_security.CyberSecurityContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CyberSecurityQueryRepository@Inject constructor(private val cyberSecurityDao: CyberSecurityDao, private val favoriteQueryRepository: FavoriteQueryRepository){

    suspend fun insertAllCyberSecurityLanguage(cyberSecurityLanguage: List<CyberSecurityLanguage>){

        CoroutineScope(Dispatchers.IO).launch {

            cyberSecurityDao.deleteAll()

            val uuid = cyberSecurityDao.insertAll(*cyberSecurityLanguage.toTypedArray())
            cyberSecurityLanguage.forEachIndexed { index, cyberSecurityLanguage ->

                cyberSecurityLanguage.uuid = uuid[index].toInt()
            }
        }
    }
    suspend fun getAllCyberSecurityLanguage():List<CyberSecurityLanguage>{

        return withContext(Dispatchers.IO){

            cyberSecurityDao.getAllCyberSecurityLanguage()
        }
    }
    suspend fun getCyberSecurityLanguage(uuid:Int): CyberSecurityLanguage {

        return withContext(Dispatchers.IO){

            cyberSecurityDao.getCyberSecurityLanguage(uuid)
        }
    }
    suspend fun getComparedCyberSecurityLanguage(compared:Boolean): List<CyberSecurityLanguage>{

        return withContext(Dispatchers.IO){
            cyberSecurityDao.getComparedCyberSecurityLanguage(compared)
        }
    }
    suspend fun updateCyberSecurityToCompare(cyberSecurityLanguage: CyberSecurityLanguage, view: View){

        CoroutineScope(Dispatchers.IO).launch {

            if(!cyberSecurityLanguage.compared){

                cyberSecurityLanguage.compared = true
                cyberSecurityDao.updateCyberSecurityLanguage(cyberSecurityLanguage) // Compared yaptıktan sonra güncelle

                val comparedAiLanguage = cyberSecurityDao.getComparedCyberSecurityLanguage(true)

                if(comparedAiLanguage.size == 2){

                    withContext(Dispatchers.Main){
                        delay(100)
                        val action = CyberSecurityContentFragmentDirections.actionCyberSecurityContentFragmentToCompareFragment("cyber_security")
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
            else{

                cyberSecurityLanguage.compared = false
                cyberSecurityDao.updateCyberSecurityLanguage(cyberSecurityLanguage) // Compared yaptıktan sonra güncelle
            }
        }

    }

    suspend fun updateCyberSecurityLanguageToStar(cyberSecurityLanguage: CyberSecurityLanguage?){

        CoroutineScope(Dispatchers.IO).launch {

            cyberSecurityLanguage?.let {

                if(!cyberSecurityLanguage.favorites){

                    cyberSecurityLanguage.favorites = true
                    cyberSecurityDao.updateCyberSecurityLanguage(cyberSecurityLanguage)

                    val favorite = Favorite(cyberSecurityLanguage.name,cyberSecurityLanguage.imageUrl)
                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{

                    cyberSecurityLanguage.favorites = false
                    cyberSecurityDao.updateCyberSecurityLanguage(cyberSecurityLanguage)

                    favoriteQueryRepository.deleteFavorites(cyberSecurityLanguage.name)
                }
            }
        }
    }
    suspend fun makeComparedFalse(comparedCyberSecurityLanguage:List<CyberSecurityLanguage>){

        CoroutineScope(Dispatchers.IO).launch{

            comparedCyberSecurityLanguage[0].compared = false
            comparedCyberSecurityLanguage[1].compared = false

            cyberSecurityDao.updateCyberSecurityLanguage(comparedCyberSecurityLanguage[0])
            cyberSecurityDao.updateCyberSecurityLanguage(comparedCyberSecurityLanguage[1])
        }
    }
}