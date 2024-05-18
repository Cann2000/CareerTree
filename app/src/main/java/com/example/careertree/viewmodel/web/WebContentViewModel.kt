package com.example.careertree.viewmodel.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.salary.Salary
import com.example.careertree.repository.APIRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class WebContentViewModel @Inject constructor(private val apiRepository: APIRepository):ViewModel() {

    val salaryList = MutableLiveData<Resource<List<Salary>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()

    private var exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
        println(throwable.localizedMessage)
    }

    fun getDataFromInternet(){

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val salary = apiRepository.getMainDataAPI("webSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }

            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }
    }

    private fun showSalary(salary: List<Salary>){

        viewModelScope.launch {

            salaryList.value = Resource.success(salary)
        }
    }
}