package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.EventListHolder
import com.example.careertree.databinding.RecyclerRowEventListBinding
import com.example.careertree.model.event.Event
import com.example.careertree.repository.EventQueryRepository

class EventListAdapter(val eventList:ArrayList<Event>): RecyclerView.Adapter<EventListHolder>() {

    lateinit var eventQueryRepository: EventQueryRepository

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListHolder {

        val binding = DataBindingUtil.inflate<RecyclerRowEventListBinding>(LayoutInflater.from(parent.context), R.layout.recycler_row_event_list,parent,false)
        return EventListHolder(binding,eventQueryRepository)
    }

    override fun onBindViewHolder(holder: EventListHolder, position: Int) {

        holder.binding.event = eventList[position]
        holder.binding.listener = holder
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}