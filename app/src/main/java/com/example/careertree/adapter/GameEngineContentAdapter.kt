package com.example.careertree.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.adapter.holders.GameEngineHolder
import com.example.careertree.databinding.RecyclerRowGameEngineContentBinding
import com.example.careertree.model.game.GameEngine
import com.example.careertree.repository.GameEngineQueryRepository

class GameEngineContentAdapter(val gameEngineList: ArrayList<GameEngine>): RecyclerView.Adapter<GameEngineHolder>() {

    lateinit var gameEngineQueryRepository: GameEngineQueryRepository

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameEngineHolder {
        val binding = DataBindingUtil.inflate<RecyclerRowGameEngineContentBinding>(LayoutInflater.from(parent.context), R.layout.recycler_row_game_engine_content,parent,false)
        return GameEngineHolder(binding,gameEngineQueryRepository)
    }

    override fun onBindViewHolder(holder: GameEngineHolder, position: Int) {
        holder.binding.gameEngine = gameEngineList[position]
        holder.binding.listener = holder
    }

    override fun getItemCount(): Int {
        return gameEngineList.size
    }
}