package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowCybersecurityContentBinding
import com.example.careertree.repository.CyberSecurityQueryRepository
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.compareClickedUtil
import com.example.careertree.view.cyber_security.CyberSecurityContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CyberSecurityContentHolder(val binding: RecyclerRowCybersecurityContentBinding,val cyberSecurityQueryRepository: CyberSecurityQueryRepository): RecyclerView.ViewHolder(binding.root),ClickListener{

    override fun dataClicked(view: View) {

        val cyberSecurityLanguage = binding.cyberSecurityLanguage

        if(cyberSecurityLanguage != null){

            val action = CyberSecurityContentFragmentDirections.actionCyberSecurityContentFragmentToCyberSecurityDetailsFragment(cyberSecurityLanguage.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun compareClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch {

            val cyberSecurityLanguage = binding.cyberSecurityLanguage

            cyberSecurityLanguage?.let {

                binding.compareButton.compareClickedUtil(!cyberSecurityLanguage.compared)

                cyberSecurityQueryRepository.updateCyberSecurityToCompare(cyberSecurityLanguage,view)
            }
        }
    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch{

            val cyberSecurityLanguage = binding.cyberSecurityLanguage

            cyberSecurityLanguage?.let {

                //binding.starButton.starClickedUtil(!cyberSecurityLanguage.favorites)

                cyberSecurityQueryRepository.updateCyberSecurityLanguageToStar(cyberSecurityLanguage)
            }

        }
    }

}