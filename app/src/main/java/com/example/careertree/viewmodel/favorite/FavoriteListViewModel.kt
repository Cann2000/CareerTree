package com.example.careertree.viewmodel.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.repository.FavoriteQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel@Inject constructor(val favoriteQueryRepository: FavoriteQueryRepository):ViewModel() {

    val favoriteList = MutableLiveData<Resource<List<Favorite>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    private val execptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun getData(){

        viewModelScope.launch(Dispatchers.IO + execptionHandler) {

            try {

                val favorites = favoriteQueryRepository.getAllFavorites()

                favorites.isNotEmpty().let {

                    showFavorites(favorites)
                }


            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = false)
            }
        }
    }

    private fun showFavorites(favorites:List<Favorite>){

        viewModelScope.launch {

            favoriteList.value = Resource.success(favorites)
            loading.value = Resource.loading(data = false)
            errorMessage.value = Resource.error("successful",data = false)
        }
    }
}