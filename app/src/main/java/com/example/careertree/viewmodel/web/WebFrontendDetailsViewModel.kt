package com.example.careertree.viewmodel.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.web.WebFrontendLanguage
import com.example.careertree.repository.WebQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebFrontendDetailsViewModel @Inject constructor(private val webQueryRepository: WebQueryRepository):ViewModel() {

    val webLanguageLiveData = MutableLiveData<Resource<WebFrontendLanguage>>()

    fun getRoomData(uuid:Int){

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val webFrontendLanguage = webQueryRepository.getWebFrontendLanguage(uuid)
                showWebFrontendLanguage(webFrontendLanguage)
            }
            catch (e:Exception){
                println(e.localizedMessage)
            }
        }
    }

    private fun showWebFrontendLanguage(webFrontendLanguage: WebFrontendLanguage){

        viewModelScope.launch {

            webLanguageLiveData.value = Resource.success(webFrontendLanguage)
        }
    }
}