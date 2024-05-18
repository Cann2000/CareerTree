package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.WebBackendContentHolder
import com.example.careertree.adapter.holders.WebFrontendContentHolder
import com.example.careertree.databinding.RecyclerRowWebbackendContentBinding
import com.example.careertree.databinding.RecyclerRowWebfrontendContentBinding
import com.example.careertree.model.web.WebBackendLanguage
import com.example.careertree.model.web.WebFrontendLanguage
import com.example.careertree.repository.WebQueryRepository

import java.lang.IllegalArgumentException

class WebContentAdapter(var webList:ArrayList<Any>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var webQueryRepository: WebQueryRepository

    private val BACKEND = 1
    private val FRONTEND = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when(viewType){

            BACKEND ->{
                val binding = DataBindingUtil.inflate<RecyclerRowWebbackendContentBinding>(
                    inflater,
                    R.layout.recycler_row_webbackend_content,parent,false)

                WebBackendContentHolder(binding,webQueryRepository)
            }
            FRONTEND->{

                val binding = DataBindingUtil.inflate<RecyclerRowWebfrontendContentBinding>(
                    inflater,
                    R.layout.recycler_row_webfrontend_content,parent,false)

                WebFrontendContentHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){

            is WebBackendContentHolder -> {

                val language = webList[position] as WebBackendLanguage
                holder.binding.web = language
                holder.binding.listener = holder
            }
            is WebFrontendContentHolder ->{

                val language = webList[position] as WebFrontendLanguage
                holder.binding.web = language
                holder.binding.listener = holder

            }
        }
    }


    override fun getItemCount(): Int {
        return webList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(webList[position]){

            is WebBackendLanguage -> BACKEND
            is WebFrontendLanguage -> FRONTEND
            else -> throw IllegalArgumentException("Unknown data type")
        }
    }
}