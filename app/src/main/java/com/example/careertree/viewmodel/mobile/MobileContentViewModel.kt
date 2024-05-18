package com.example.careertree.viewmodel.mobile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.mobile.MobileLanguage
import com.example.careertree.repository.APIRepository
import com.example.careertree.utility.Resource
import com.example.careertree.model.salary.Salary
import com.example.careertree.repository.MobileQueryRepository
import com.example.careertree.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobileContentViewModel @Inject constructor(
    private val apiRepository: APIRepository,
    private val mobileQueryRepository: MobileQueryRepository, ):ViewModel() {

    val salaryList = MutableLiveData<Resource<List<Salary>>>()
    val languageList = MutableLiveData<Resource<List<MobileLanguage>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun getData(){

        if (Constants.loadAndroid){

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

                val salary = apiRepository.getMainDataAPI("androidSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }

                val androidLanguages = apiRepository.getMainDataAPI("android")

                androidLanguages.data?.let {

                    saveToSql(it.map { it as MobileLanguage }).apply {

                        Constants.loadAndroid = false

                        showLanguage(it.map { it as MobileLanguage })
                    }
                }

            }catch (e:Exception){

                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }


    }

    private fun getDataFromSql(){

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val androidLanguages = mobileQueryRepository.getAllMobileLanguage()

                androidLanguages.isNotEmpty().let {

                    showLanguage(androidLanguages)

                }

                val salary = apiRepository.getMainDataAPI("androidSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }
            }
            catch (e:Exception){

                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }
    }

    private suspend fun saveToSql(languageList: List<MobileLanguage>){

        mobileQueryRepository.insertAllLanguage(languageList)
    }

    private fun showLanguage(language: List<MobileLanguage>){

        viewModelScope.launch {

            languageList.value = Resource.success(language)
            loading.value = Resource.loading(data = false)
            errorMessage.value = Resource.error("successful",data = false)
        }
    }

    private fun showSalary(salary: List<Salary>){

        viewModelScope.launch {

            salaryList.value = Resource.success(salary)
        }
    }

}