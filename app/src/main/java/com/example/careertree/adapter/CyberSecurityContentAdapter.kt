package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.CyberSecurityContentHolder
import com.example.careertree.databinding.RecyclerRowCybersecurityContentBinding
import com.example.careertree.model.cyber_security.CyberSecurityLanguage
import com.example.careertree.repository.CyberSecurityQueryRepository

class CyberSecurityContentAdapter(val cyberSecurityLanguageList:ArrayList<CyberSecurityLanguage>): RecyclerView.Adapter<CyberSecurityContentHolder>() {

    lateinit var cyberSecurityQueryRepository: CyberSecurityQueryRepository


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CyberSecurityContentHolder {
        val binding = DataBindingUtil.inflate<RecyclerRowCybersecurityContentBinding>(LayoutInflater.from(parent.context),
            R.layout.recycler_row_cybersecurity_content,parent,false)

        return CyberSecurityContentHolder(binding,cyberSecurityQueryRepository)
    }

    override fun onBindViewHolder(holder: CyberSecurityContentHolder, position: Int) {

        holder.binding.cyberSecurityLanguage = cyberSecurityLanguageList[position]
        holder.binding.listener = holder
    }

    override fun getItemCount(): Int {
        return cyberSecurityLanguageList.size
    }
}