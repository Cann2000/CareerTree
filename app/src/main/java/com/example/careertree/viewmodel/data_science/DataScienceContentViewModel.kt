package com.example.careertree.viewmodel.data_science

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.data_science.DataScienceLanguage
import com.example.careertree.model.salary.Salary
import com.example.careertree.repository.APIRepository
import com.example.careertree.repository.DataScienceQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataScienceContentViewModel @Inject constructor(
    private val dataScienceQueryRepository: DataScienceQueryRepository,
    private val apiRepository: APIRepository
) : ViewModel() {

    val languageList = MutableLiveData<Resource<List<DataScienceLanguage>>>()
    val salaryList = MutableLiveData<Resource<List<Salary>>>()
    val loading = MutableLiveData<Resource<Boolean>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, true)
    }

    fun getData() {

        if (Constants.loadDataScienceLanguage) {

            getDataFromInternet()

        } else {

            getDataFromSql()
        }
    }

    private fun getDataFromInternet() {

        loading.value = Resource.loading(true)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val dataScienceLanguageList = apiRepository.getMainDataAPI("dataScience")

                dataScienceLanguageList.data?.let {

                    saveToSql(it.map { it as DataScienceLanguage }).apply {

                        Constants.loadDataScienceLanguage = false

                        showCyberSecurityLanguage(it.map { it as DataScienceLanguage })
                    }
                }

                val salary = apiRepository.getMainDataAPI("dataScienceSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }
            }
            catch (e: Exception) {

                viewModelScope.launch {

                    errorMessage.value = Resource.error(e.localizedMessage, true)

                }
            }
        }
    }

    private fun getDataFromSql() {

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val cyberSecurityLanguageList = dataScienceQueryRepository.getAllDataScienceLanguage()

                cyberSecurityLanguageList.isNotEmpty().let {

                    showCyberSecurityLanguage(cyberSecurityLanguageList)
                }

                val salary = apiRepository.getMainDataAPI("dataScienceSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }

            } catch (e: Exception) {

                errorMessage.value = Resource.error(e.localizedMessage, true)
            }
        }
    }

    private suspend fun saveToSql(dataScienceLanguageList: List<DataScienceLanguage>) {

        dataScienceQueryRepository.insertAllDataScienceLanguage(dataScienceLanguageList)
    }

    private fun showSalary(salary: List<Salary>) {

        viewModelScope.launch {

            salaryList.value = Resource.success(salary)
        }
    }

    private fun showCyberSecurityLanguage(dataScienceLanguageList: List<DataScienceLanguage>) {

        viewModelScope.launch {

            languageList.value = Resource.success(dataScienceLanguageList)
            loading.value = Resource.loading(false)
            errorMessage.value = Resource.error("successful", false)
        }
    }
}