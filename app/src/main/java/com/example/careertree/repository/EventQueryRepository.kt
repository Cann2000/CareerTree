package com.example.careertree.repository

import com.example.careertree.model.event.Event
import com.example.careertree.service.dao.EventDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventQueryRepository@Inject constructor(private val eventDao: EventDao){

    suspend fun insertAll(eventList: List<Event>){

        CoroutineScope(Dispatchers.IO).launch{

            val followedEventList = eventDao.getFollowedEvents(true)

            eventDao.deleteAll()

            try {
                val uuid = eventDao.insertAll(*eventList.toTypedArray())
                eventList.forEachIndexed { index, event ->
                    event.uuid = uuid[index].toInt()

                    followedEventList.isNotEmpty().let {

                        for (followedEvent in followedEventList){

                            if(event.name == followedEvent.name){

                                event.followedEvent = true
                                eventDao.updateEvent(event)
                            }

                        }

                    }
                }

            } catch (e: Exception) {

                println(e.printStackTrace())
            }
        }
    }

    suspend fun getFollowedEvent():List<Event>{

        return withContext(Dispatchers.IO){

            eventDao.getFollowedEvents(true)
        }
    }

    suspend fun getAllEvent():List<Event> {

        return withContext(Dispatchers.IO){

            eventDao.getAllEvent()
        }
    }

    suspend fun updateFollowedEvent(event: Event){

        CoroutineScope(Dispatchers.IO).launch {

            if(!event.followedEvent){

                event.followedEvent = true
                eventDao.updateEvent(event)

            }
            else{

                event.followedEvent = false
                eventDao.updateEvent(event)
            }
        }
    }
}