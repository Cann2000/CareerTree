package com.example.careertree.viewmodel.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.event.Event
import com.example.careertree.repository.EventQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowedEventListViewModel@Inject constructor(private val eventQueryRepository: EventQueryRepository):ViewModel() {

    val eventListLiveData = MutableLiveData<Resource<List<Event>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    private val exceptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun getData(){

        loading.value = Resource.loading(true)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {

                val eventList = eventQueryRepository.getFollowedEvent()

                eventList.isNotEmpty().let {

                    showEvents(eventList)
                }

            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)

            }
        }
    }

    fun showEvents(eventList:List<Event>){

        viewModelScope.launch {

            eventListLiveData.value = Resource.success(eventList)
            errorMessage.value = Resource.error("successful",data = false)
            loading.value = Resource.loading(false)
        }
    }
}