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
import com.example.careertree.databinding.RecyclerRowLibraryBinding

class LibraryListAdapter(val libraryList:ArrayList<String>):RecyclerView.Adapter<LibraryListAdapter.LibraryHolder>() {

    class LibraryHolder(val binding: RecyclerRowLibraryBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
        val binding = RecyclerRowLibraryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LibraryHolder(binding)
    }

    override fun onBindViewHolder(holder: LibraryHolder, position: Int) {


        holder.binding.libraryName.text = libraryList[position]

        val coloredText = " -> "
        val startIndex = libraryList[position].indexOf(coloredText)

        if (startIndex != -1) {
            val beforeSeparator = libraryList[position].substring(0, startIndex)
            val spannable = SpannableString(libraryList[position])

            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#DFE80E0E")), 0, beforeSeparator.length ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD), 0, beforeSeparator.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(0.95f), 0, beforeSeparator.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            holder.binding.libraryName.text = spannable
        }
    }

    override fun getItemCount(): Int {
        return libraryList.size
    }
}