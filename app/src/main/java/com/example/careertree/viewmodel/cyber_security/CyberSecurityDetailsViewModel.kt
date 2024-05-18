package com.example.careertree.viewmodel.cyber_security

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.cyber_security.CyberSecurityLanguage
import com.example.careertree.repository.CyberSecurityQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CyberSecurityDetailsViewModel@Inject constructor(
    private val cyberSecurityQueryRepository: CyberSecurityQueryRepository
): ViewModel() {

    val cyberSecurityLanguageLiveData = MutableLiveData<Resource<CyberSecurityLanguage>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()

    fun getRoomData(uuid:Int){

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val cyberSecurityLanguage = cyberSecurityQueryRepository.getCyberSecurityLanguage(uuid)
                showCyberSecurityLanguage(cyberSecurityLanguage)
            }
            catch (e:Exception){

                errorMessage.value = Resource.error(e.localizedMessage,true)
            }
        }
    }

    private fun showCyberSecurityLanguage(cyberSecurityLanguage: CyberSecurityLanguage){

        viewModelScope.launch {

            cyberSecurityLanguageLiveData.value = Resource.success(cyberSecurityLanguage)
            errorMessage.value = Resource.error("successful",false)
        }
    }
}