package com.example.careertree.repository

import android.view.View
import androidx.navigation.Navigation
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.model.mobile.MobileLanguage
import com.example.careertree.service.dao.MobileDao
import com.example.careertree.view.mobile.MobileContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MobileQueryRepository@Inject constructor(
    private val mobileDao: MobileDao,
    private val favoriteQueryRepository: FavoriteQueryRepository) {

    suspend fun insertAllLanguage(languageList: List<MobileLanguage>){

        CoroutineScope(Dispatchers.IO).launch{

            mobileDao.deleteAll()

            val uuid = mobileDao.insertAll(*languageList.toTypedArray())
            languageList.forEachIndexed { index, language ->
                language.uuid = uuid[index].toInt()
            }

        }
    }

    suspend fun getMobileLanguage(uuid:Int): MobileLanguage {

        return withContext(Dispatchers.IO){

            mobileDao.getMobileLanguage(uuid)
        }
    }

    suspend fun getAllMobileLanguage():List<MobileLanguage>{

        return withContext(Dispatchers.IO){

            mobileDao.getAllMobileLanguage()
        }
    }

    suspend fun getComparedLanguage(compared:Boolean):List<MobileLanguage>{

        return withContext(Dispatchers.IO){

            mobileDao.getComparedMobileLanguage(compared)
        }
    }

    suspend fun updateMobileLanguageToCompare(mobileLanguage: MobileLanguage,view: View){

        CoroutineScope(Dispatchers.IO).launch {

            if(!mobileLanguage.compared){

                mobileLanguage.compared = true
                mobileDao.updateMobileLanguage(mobileLanguage) // Compared yaptıktan sonra güncelle

                val comparedAndroidLanguage = mobileDao.getComparedMobileLanguage(true)

                if(comparedAndroidLanguage.size == 2){

                    withContext(Dispatchers.Main){
                        delay(100)
                        val action = MobileContentFragmentDirections.actionMobileContentFragmentToCompareFragment("android")
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
            else{

                mobileLanguage.compared = false
                mobileDao.updateMobileLanguage(mobileLanguage) // Compared yaptıktan sonra güncelle
            }
        }

    }
    suspend fun updateMobileLanguageToStar(mobileLanguage: MobileLanguage?){

        CoroutineScope(Dispatchers.IO).launch {

            mobileLanguage?.let {

                if(!mobileLanguage.favorites){

                    mobileLanguage.favorites = true
                    mobileDao.updateMobileLanguage(mobileLanguage)

                    val favorite = Favorite(mobileLanguage.name,mobileLanguage.imageUrl)
                    favoriteQueryRepository.insertFavorite(favorite)
                }
                else{

                    mobileLanguage.favorites = false
                    mobileDao.updateMobileLanguage(mobileLanguage)

                    favoriteQueryRepository.deleteFavorites(mobileLanguage.name)
                }
            }
        }
    }
    suspend fun makeComparedFalse(comparedMobileLanguage:List<MobileLanguage>){

        CoroutineScope(Dispatchers.IO).launch{

            comparedMobileLanguage[0].compared = false
            comparedMobileLanguage[1].compared = false

            mobileDao.updateMobileLanguage(comparedMobileLanguage[0])
            mobileDao.updateMobileLanguage(comparedMobileLanguage[1])
        }
    }
}

