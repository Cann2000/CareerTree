package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowWebfrontendContentBinding
import com.example.careertree.utility.ClickListener
import com.example.careertree.view.web.WebFrontendContentFragmentDirections

class WebFrontendContentHolder(val binding: RecyclerRowWebfrontendContentBinding): RecyclerView.ViewHolder(binding.root),
    ClickListener {
    override fun dataClicked(view: View) {

        val web = binding.web

        if(web!=null){

            val action = WebFrontendContentFragmentDirections.actionFrontendContentFragmentToWebFrontendDetailsFragment(web.uuid!!)
            Navigation.findNavController(view).navigate(action)

        }
    }

    override fun compareClicked(view: View) {
        TODO("Not yet implemented")
    }

    override fun starClicked(view: View) {
        TODO("Not yet implemented")
    }
}