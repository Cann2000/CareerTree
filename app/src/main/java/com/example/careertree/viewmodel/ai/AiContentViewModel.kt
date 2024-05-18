package com.example.careertree.viewmodel.ai

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.salary.Salary
import com.example.careertree.model.ai.AiLanguage
import com.example.careertree.repository.APIRepository
import com.example.careertree.repository.AiQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiContentViewModel@Inject constructor(
    private val aiQueryRepository: AiQueryRepository,
    private val apiRepository: APIRepository) :ViewModel() {

    val languageList = MutableLiveData<Resource<List<AiLanguage>>>()
    val salaryList = MutableLiveData<Resource<List<Salary>>>()
    val loading = MutableLiveData<Resource<Boolean>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage,true)
    }

    fun getData(){

        if(Constants.loadAiLanguage){

            getDataFromInternet()
        }
        else{

            getDataFromSql()
        }
    }

    private fun getDataFromInternet(){

        loading.value = Resource.loading(true)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler){

            try {

                val aiLanguageList = apiRepository.getMainDataAPI("ai")

                aiLanguageList.data?.let {

                    saveToSql(it.map { it as AiLanguage }).apply {

                        Constants.loadAiLanguage = false

                        showAiLanguage(it.map { it as AiLanguage })
                    }
                }

                val salary = apiRepository.getMainDataAPI("aiSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }
            }
            catch (e:Exception){

                errorMessage.value = Resource.error(e.localizedMessage,true)
            }
        }
    }
    private fun getDataFromSql(){

        viewModelScope.launch(Dispatchers.IO + exceptionHandler){

            try {

                val aiLanguageList = aiQueryRepository.getAllAiLanguage()

                aiLanguageList.isNotEmpty().let {

                    showAiLanguage(aiLanguageList)
                }

                val salary = apiRepository.getMainDataAPI("aiSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }
            }
            catch (e:Exception){

                errorMessage.value = Resource.error(e.localizedMessage,true)
            }
        }
    }

    private suspend fun saveToSql(aiLanguageList: List<AiLanguage>){

        aiQueryRepository.insertAllAiLanguage(aiLanguageList)
    }

    private fun showSalary(salary:List<Salary>){

        viewModelScope.launch {

            salaryList.value = Resource.success(salary)
        }
    }

    private fun showAiLanguage(aiLanguageList: List<AiLanguage>){

        viewModelScope.launch {

            languageList.value = Resource.success(aiLanguageList)
            loading.value = Resource.loading(false)
            errorMessage.value = Resource.error("successful",false)
        }
    }

}