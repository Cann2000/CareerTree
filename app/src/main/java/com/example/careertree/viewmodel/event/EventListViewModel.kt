package com.example.careertree.viewmodel.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.event.Event
import com.example.careertree.repository.APIRepository
import com.example.careertree.repository.EventQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val eventQueryRepository: EventQueryRepository,
    private val apiRepository: APIRepository):ViewModel() {

    val eventListLiveData = MutableLiveData<Resource<List<Event>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    private val execptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun getData(){

        if(Constants.loadEventList){

            getDataFromInternet()
        }
        else{

            getDataFromSql()
        }
    }
    private fun getDataFromInternet(){

        loading.value = Resource.loading(data = true)

        viewModelScope.launch(Dispatchers.IO + execptionHandler) {

            try {
                val eventList = apiRepository.getMainDataAPI("events")

                eventList.data?.let {

                    saveToSql(it.map { it as Event }).apply {

                        Constants.loadEventList = false

                        delay(100)
                        showEvents(it.map { it as Event })
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = false)
            }
        }
    }

    private fun getDataFromSql(){

        loading.value = Resource.loading(data = true)

        viewModelScope.launch(Dispatchers.IO + execptionHandler) {

            try {
                val eventList = eventQueryRepository.getAllEvent()

                eventList.isNotEmpty().let {

                    showEvents(eventList)
                }
            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }

        }
    }
    private suspend fun saveToSql(eventList: List<Event>){

        eventQueryRepository.insertAll(eventList)
    }
    fun showEvents(eventList:List<Event>){

        viewModelScope.launch {

            eventListLiveData.value = Resource.success(eventList)
            loading.value = Resource.loading(data = false)
            errorMessage.value = Resource.error("successful",data = false)
        }
    }

}