package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.AiContentHolder
import com.example.careertree.databinding.RecyclerRowAiContentBinding
import com.example.careertree.model.ai.AiLanguage
import com.example.careertree.repository.AiQueryRepository

class AiContentAdapter(val aiLanguageList:ArrayList<AiLanguage>):RecyclerView.Adapter<AiContentHolder>() {

    lateinit var aiQueryRepository: AiQueryRepository

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiContentHolder {

        val binding = DataBindingUtil.inflate<RecyclerRowAiContentBinding>(LayoutInflater.from(parent.context),R.layout.recycler_row_ai_content,parent,false)
        return AiContentHolder(binding,aiQueryRepository)
    }

    override fun onBindViewHolder(holder: AiContentHolder, position: Int) {

        holder.binding.ai = aiLanguageList[position]
        holder.binding.listener = holder
    }

    override fun getItemCount(): Int {
        return aiLanguageList.size
    }
}