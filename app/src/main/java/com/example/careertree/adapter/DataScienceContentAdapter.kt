package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.DataScienceContentHolder
import com.example.careertree.databinding.RecyclerRowDatascienceContentBinding
import com.example.careertree.model.data_science.DataScienceLanguage
import com.example.careertree.repository.DataScienceQueryRepository

class DataScienceContentAdapter(val dataScienceLanguageList:ArrayList<DataScienceLanguage>): RecyclerView.Adapter<DataScienceContentHolder>() {

    lateinit var dataScienceQueryRepository: DataScienceQueryRepository


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataScienceContentHolder {
        val binding = DataBindingUtil.inflate<RecyclerRowDatascienceContentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recycler_row_datascience_content,parent,false)

        return DataScienceContentHolder(binding,dataScienceQueryRepository)
    }

    override fun onBindViewHolder(holder: DataScienceContentHolder, position: Int) {

        holder.binding.dataScienceLanguage = dataScienceLanguageList[position]
        holder.binding.listener = holder
    }

    override fun getItemCount(): Int {
        return dataScienceLanguageList.size
    }
}