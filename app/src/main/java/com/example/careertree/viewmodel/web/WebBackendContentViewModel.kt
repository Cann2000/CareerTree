package com.example.careertree.viewmodel.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.web.WebBackendLanguage
import com.example.careertree.repository.APIRepository
import com.example.careertree.repository.WebQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebBackendContentViewModel @Inject constructor(private val apiRepository: APIRepository, private val webQueryRepository: WebQueryRepository) :ViewModel() {

    val languageList = MutableLiveData<Resource<List<WebBackendLanguage>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun getData(){

        if(Constants.loadWebBackend){

            getDataFromInternet()
        }
        else{

            getDataFromSql()
        }
    }
    private fun getDataFromInternet(){

        loading.value = Resource.loading(data = true)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val language = apiRepository.getMainDataAPI("webBackend")

                language.data?.let {

                    saveToSql(it.map { it as WebBackendLanguage }).apply {

                        Constants.loadWebBackend = false

                        showLanguage(it.map { it as WebBackendLanguage })
                    }
                }

            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }
    }
    private fun getDataFromSql(){

        loading.value = Resource.loading(data = true)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val language = webQueryRepository.getAllWebBackendLanguage()

                language.isNotEmpty().let {

                    showLanguage(language)
                }


            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }
    }

    private suspend fun saveToSql(languageList:List<WebBackendLanguage>){

        webQueryRepository.insertAllWebBackend(languageList)
    }

    private fun showLanguage(language: List<WebBackendLanguage>){

        viewModelScope.launch {

            languageList.value = Resource.success(language)
            loading.value = Resource.loading(data = false)
            errorMessage.value = Resource.error("successful", data = false)
        }
    }

}