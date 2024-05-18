package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.MobileContentHolder
import com.example.careertree.databinding.RecyclerRowMobileContentBinding
import com.example.careertree.model.mobile.MobileLanguage
import com.example.careertree.repository.MobileQueryRepository

class MobileContentAdapter(val languageList: ArrayList<MobileLanguage>):RecyclerView.Adapter<MobileContentHolder>() {

    lateinit var mobileQueryRepository: MobileQueryRepository

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobileContentHolder {

        val binding = DataBindingUtil.inflate<RecyclerRowMobileContentBinding>(LayoutInflater.from(parent.context),R.layout.recycler_row_mobile_content,parent,false)
        return MobileContentHolder(binding,mobileQueryRepository)
    }


    override fun onBindViewHolder(holder: MobileContentHolder, position: Int) {

        holder.binding.mobileLanguage = languageList[position]
        holder.binding.listener = holder
    }

    override fun getItemCount(): Int {
        return languageList.size
    }
}