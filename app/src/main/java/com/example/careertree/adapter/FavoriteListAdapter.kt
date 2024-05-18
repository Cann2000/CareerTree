package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.databinding.RecyclerRowFavoriteBinding
import com.example.careertree.model.favorite.Favorite

class FavoriteListAdapter(val favoriteList:ArrayList<Favorite>): RecyclerView.Adapter<FavoriteListAdapter.FavoriteHolder>() {

    class FavoriteHolder(val binding: RecyclerRowFavoriteBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val binding = DataBindingUtil.inflate<RecyclerRowFavoriteBinding>(LayoutInflater.from(parent.context), R.layout.recycler_row_favorite,parent,false)
        return FavoriteHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.binding.favorite = favoriteList[position]
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }
}