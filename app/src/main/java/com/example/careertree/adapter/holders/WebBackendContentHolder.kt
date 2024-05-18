package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowWebbackendContentBinding
import com.example.careertree.repository.WebQueryRepository
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.compareClickedUtil
import com.example.careertree.view.web.WebBackendContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebBackendContentHolder(val binding: RecyclerRowWebbackendContentBinding,private val webQueryRepository: WebQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener {
    override fun dataClicked(view: View) {

        val web = binding.web

        if(web!=null){

            val action = WebBackendContentFragmentDirections.actionBackendContentFragmentToWebBackendDetailsFragment(web.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun compareClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch {

            val webBackendLanguage = binding.web

            webBackendLanguage?.let {

                binding.compareButton.compareClickedUtil(!webBackendLanguage.compared)

                webQueryRepository.updateWebBackendLanguageToCompare(webBackendLanguage,view)
            }
        }

    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch {

            val webBackendLanguage = binding.web

            webBackendLanguage?.let {

                //binding.starButton.starClickedUtil(!webBackendLanguage.favorites)

                webQueryRepository.updateWebBackendToStar(webBackendLanguage)
            }
        }
    }
}