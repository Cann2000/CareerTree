package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.HomePageContentHolder
import com.example.careertree.databinding.RecyclerRowHomepageBinding
import com.example.careertree.model.homepage.HomePageContent

class HomePageAdapter(private val contentList:ArrayList<HomePageContent>): RecyclerView.Adapter<HomePageContentHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageContentHolder {
        val binding = DataBindingUtil.inflate<RecyclerRowHomepageBinding>(LayoutInflater.from(parent.context), R.layout.recycler_row_homepage,parent,false)
        return HomePageContentHolder(binding)
    }


    override fun onBindViewHolder(holder: HomePageContentHolder, position: Int) {

        holder.binding.content = contentList[position]
        holder.binding.listener = holder

    }

    override fun getItemCount(): Int {
        return contentList.size
    }

}