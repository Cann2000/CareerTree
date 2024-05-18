package com.example.careertree.viewmodel.data_science

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.data_science.DataScienceLanguage
import com.example.careertree.repository.DataScienceQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataScienceDetailsViewModel@Inject constructor(
    private val dataScienceQueryRepository: DataScienceQueryRepository
): ViewModel() {

    val dataScienceLanguageLiveData = MutableLiveData<Resource<DataScienceLanguage>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()

    fun getRoomData(uuid:Int){

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val cyberSecurityLanguage = dataScienceQueryRepository.getDataScienceLanguage(uuid)
                showCyberDataScienceLanguage(cyberSecurityLanguage)
            }
            catch (e:Exception){
                errorMessage.value = Resource.error(e.localizedMessage,true)
            }
        }
    }

    private fun showCyberDataScienceLanguage(dataScienceLanguage: DataScienceLanguage){

        viewModelScope.launch {

            dataScienceLanguageLiveData.value = Resource.success(dataScienceLanguage)
            errorMessage.value = Resource.error("successful",false)
        }
    }
}