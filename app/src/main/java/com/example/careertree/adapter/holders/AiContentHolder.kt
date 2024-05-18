package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowAiContentBinding
import com.example.careertree.repository.AiQueryRepository
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.compareClickedUtil
import com.example.careertree.view.ai.AiContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AiContentHolder(val binding: RecyclerRowAiContentBinding, val aiQueryRepository: AiQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener {

    override fun dataClicked(view: View) {

        val aiLanguage = binding.ai

        if(aiLanguage != null){

            val action = AiContentFragmentDirections.actionAiContentFragmentToAiDetailsFragment(aiLanguage.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun compareClicked(view: View) {

      CoroutineScope(Dispatchers.Main).launch {

          val aiLanguage = binding.ai

          aiLanguage?.let {

              binding.compareButton.compareClickedUtil(!aiLanguage.compared)

              aiQueryRepository.updateAiLanguageToCompare(aiLanguage,view)
          }
      }
    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch{

            val aiLanguage = binding.ai

            aiLanguage?.let {

                //binding.starButton.starClickedUtil(!aiLanguage.favorites)

                aiQueryRepository.updateAiLanguageToStar(aiLanguage)

            }

        }
    }
}