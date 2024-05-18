package com.example.careertree.viewmodel.gemini_ai

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AiChatBotViewModel:ViewModel() {

    val aiTextResponse = MutableLiveData<String>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun geminiAiChatBotResult(question:String){

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            if(question.isNotEmpty()){

                viewModelScope.launch {

                    loading.value = Resource.loading(true)
                }

                try {
                    val generativeModel = GenerativeModel(
                        modelName = Constants.Gemini_ModelName,
                        apiKey = Constants.Gemini_API
                    )

                    val response = generativeModel.generateContent(question)
                    showResult(response.text.toString())
                }
                catch (e:Exception){
                    errorMessage.value = Resource.error(e.localizedMessage,true)
                }
            }
        }
    }

    private fun showResult(response:String){

        viewModelScope.launch {
            aiTextResponse.value = response
            errorMessage.value = Resource.error("successful",false)
            loading.value = Resource.loading(false)
        }

    }
}