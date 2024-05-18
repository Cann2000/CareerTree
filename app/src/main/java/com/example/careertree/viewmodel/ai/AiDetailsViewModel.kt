package com.example.careertree.viewmodel.ai

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.ai.AiLanguage
import com.example.careertree.repository.AiQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiDetailsViewModel@Inject constructor(
    private val aiQueryRepository: AiQueryRepository):ViewModel() {

    val aiLanguageLiveData = MutableLiveData<Resource<AiLanguage>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()

    fun getRoomData(uuid:Int){

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val aiLanguage = aiQueryRepository.getAiLanguage(uuid)
                showAiLanguage(aiLanguage)
            }
            catch (e:Exception){
                errorMessage.value = Resource.error(e.localizedMessage,true)
            }
        }
    }

    private fun showAiLanguage(aiLanguage: AiLanguage){

        viewModelScope.launch {

            aiLanguageLiveData.value = Resource.success(aiLanguage)
            errorMessage.value = Resource.error("successful",false)
        }
    }
}