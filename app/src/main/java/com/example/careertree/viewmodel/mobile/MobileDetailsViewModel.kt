package com.example.careertree.viewmodel.mobile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.mobile.MobileLanguage
import com.example.careertree.repository.MobileQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MobileDetailsViewModel @Inject constructor(private val mobileQueryRepository: MobileQueryRepository ):ViewModel() {

    val mobileLanguageLiveData = MutableLiveData<Resource<MobileLanguage>>()


    fun getRoomData(uuid: Int){

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val language = mobileQueryRepository.getMobileLanguage(uuid)
                showAndroidLanguage(language)
            }
            catch (e:Exception){
                println(e.localizedMessage)
            }

        }
    }

    private fun showAndroidLanguage(language:MobileLanguage){

        viewModelScope.launch {

            mobileLanguageLiveData.value = Resource.success(language)
        }
    }

}