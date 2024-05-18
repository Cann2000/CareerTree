package com.example.careertree.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.R
import com.example.careertree.databinding.RecyclerRowBlogBinding
import com.example.careertree.model.blog.Blog

class BlogContentAdapter(val blogList:ArrayList<Blog>): RecyclerView.Adapter<BlogContentAdapter.BlogContentHolder>() {

    class BlogContentHolder(val binding:RecyclerRowBlogBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogContentHolder {
        val binding = DataBindingUtil.inflate<RecyclerRowBlogBinding>(LayoutInflater.from(parent.context),R.layout.recycler_row_blog,parent ,false)
        return BlogContentHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogContentHolder, position: Int) {

        holder.binding.blog = blogList[position]

        holder.itemView.setOnClickListener {

            blogList[position].link?.isNotEmpty().let {

                if(it == true){

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(blogList[position].link))
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return blogList.size
    }
}