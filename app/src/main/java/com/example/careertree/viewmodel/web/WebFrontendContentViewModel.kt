package com.example.careertree.viewmodel.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.web.WebFrontendLanguage
import com.example.careertree.repository.APIRepository
import com.example.careertree.repository.WebQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WebFrontendContentViewModel @Inject constructor(private val apiRepository: APIRepository, private val webQueryRepository: WebQueryRepository):ViewModel() {

    val languageList = MutableLiveData<Resource<List<WebFrontendLanguage>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = false)
    }

    fun getData(){

        if(Constants.loadWebFrontend){

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

                val language = apiRepository.getMainDataAPI("webFrontend")

                withContext(Dispatchers.IO + exceptionHandler) {

                    language.data?.let {

                        saveToSql(it.map { it as WebFrontendLanguage }).apply {

                            Constants.loadWebFrontend = false

                            showLanguage(it.map { it as WebFrontendLanguage })

                        }
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

                val language = webQueryRepository.getAllWebFrontendLanguage()

                language.isNotEmpty().let {

                    withContext(Dispatchers.Main) {

                        showLanguage(language)
                    }
                }


            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }
    }

    private suspend fun saveToSql(languageList:List<WebFrontendLanguage>){

        webQueryRepository.insertAllWebFrontend(languageList)
    }

    private fun showLanguage(language:List<WebFrontendLanguage>){

        viewModelScope.launch {

            languageList.value = Resource.success(language)
            loading.value = Resource.loading(data = false)
            errorMessage.value = Resource.error("successful", data = false)

        }
    }
}