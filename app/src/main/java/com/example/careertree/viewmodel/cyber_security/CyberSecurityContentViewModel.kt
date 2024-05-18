package com.example.careertree.viewmodel.cyber_security

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.cyber_security.CyberSecurityLanguage
import com.example.careertree.model.salary.Salary
import com.example.careertree.repository.APIRepository
import com.example.careertree.repository.CyberSecurityQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CyberSecurityContentViewModel @Inject constructor(
    private val cyberSecurityQueryRepository: CyberSecurityQueryRepository,
    private val apiRepository: APIRepository
) : ViewModel() {

    val languageList = MutableLiveData<Resource<List<CyberSecurityLanguage>>>()
    val salaryList = MutableLiveData<Resource<List<Salary>>>()
    val loading = MutableLiveData<Resource<Boolean>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, true)
    }

    fun getData() {

        if (Constants.loadCyberSecurityLanguage) {

            getDataFromInternet()
        } else {

            getDataFromSql()
        }
    }

    private fun getDataFromInternet() {

        loading.value = Resource.loading(true)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {
                val cyberSecurityLanguageList = apiRepository.getMainDataAPI("cyberSecurity")

                cyberSecurityLanguageList.data?.let {

                    saveToSql(it.map { it as CyberSecurityLanguage }).apply {

                        Constants.loadCyberSecurityLanguage = false

                        showCyberSecurityLanguage(it.map { it as CyberSecurityLanguage })
                    }
                }

                val salary = apiRepository.getMainDataAPI("cyberSecuritySalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }
            } catch (e: Exception) {

                viewModelScope.launch {

                    errorMessage.value = Resource.error(e.localizedMessage, true)
                }
            }
        }
    }

    private fun getDataFromSql() {

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val cyberSecurityLanguageList = cyberSecurityQueryRepository.getAllCyberSecurityLanguage()

                cyberSecurityLanguageList.isNotEmpty().let {

                    showCyberSecurityLanguage(cyberSecurityLanguageList)
                }

                val salary = apiRepository.getMainDataAPI("cyberSecuritySalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }
            } catch (e: Exception) {

                errorMessage.value = Resource.error(e.localizedMessage, true)
            }
        }
    }

    private suspend fun saveToSql(cyberSecurityLanguageList: List<CyberSecurityLanguage>) {

        cyberSecurityQueryRepository.insertAllCyberSecurityLanguage(cyberSecurityLanguageList)
    }

    private fun showSalary(salary: List<Salary>) {

        viewModelScope.launch {

            salaryList.value = Resource.success(salary)
        }
    }

    private fun showCyberSecurityLanguage(cyberSecurityLanguageList: List<CyberSecurityLanguage>) {

        viewModelScope.launch {

            languageList.value = Resource.success(cyberSecurityLanguageList)
            loading.value = Resource.loading(false)
            errorMessage.value = Resource.error("successful", false)
        }
    }
}
