package com.example.careertree.viewmodel.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.web.WebBackendLanguage
import com.example.careertree.repository.WebQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebBackendDetailsViewModel @Inject constructor(private val webQueryRepository: WebQueryRepository):ViewModel() {

    val webLanguageLiveData = MutableLiveData<Resource<WebBackendLanguage>>()

    fun getRoomData(uuid: Int){

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val webBackendLanguage = webQueryRepository.getWebBackendLanguage(uuid)
                showWebBackendLanguage(webBackendLanguage)
            }
            catch (e:Exception){
                println(e.localizedMessage)
            }

        }
    }

    private fun showWebBackendLanguage(webBackendLanguage: WebBackendLanguage){

        viewModelScope.launch {

            webLanguageLiveData.value = Resource.success(webBackendLanguage)
        }
    }
}