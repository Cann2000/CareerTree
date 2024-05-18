package com.example.careertree.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowGamesBinding

class GameListAdapter(val gameList:ArrayList<String>):RecyclerView.Adapter<GameListAdapter.GameHolder>() {

    class GameHolder(val binding:RecyclerRowGamesBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val binding = RecyclerRowGamesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GameHolder(binding)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {

        holder.binding.gameName.text = gameList[position]

        val coloredText = " -> "
        val startIndex = gameList[position].indexOf(coloredText)
        if (startIndex != -1) {
            val beforeSeparator = gameList[position].substring(0, startIndex)
            val spannable = SpannableString(gameList[position])

            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#DFE80E0E")), 0, beforeSeparator.length ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD), 0, beforeSeparator.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(0.95f), 0, beforeSeparator.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            holder.binding.gameName.text = spannable
        }
    }

    override fun getItemCount(): Int {
        return gameList.size
    }
}