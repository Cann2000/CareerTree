package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowMobileContentBinding
import com.example.careertree.repository.MobileQueryRepository
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.compareClickedUtil
import com.example.careertree.view.mobile.MobileContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MobileContentHolder(val binding: RecyclerRowMobileContentBinding, val mobileQueryRepository: MobileQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener {

    override fun dataClicked(view: View) {

        val mobileLanguage = binding.mobileLanguage

        if(mobileLanguage!=null){

            val action = MobileContentFragmentDirections.actionMobileContentFragmentToMobileDetailsFragment(mobileLanguage.uuid!!)
            Navigation.findNavController(view).navigate(action)

        }
    }

    override fun compareClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch {

            val mobileLanguage = binding.mobileLanguage

            mobileLanguage?.let {

                binding.compareButton.compareClickedUtil(!mobileLanguage.compared)

                mobileQueryRepository.updateMobileLanguageToCompare(mobileLanguage,view)
            }


        }
    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch {

            val mobileLanguage = binding.mobileLanguage

            mobileLanguage?.let {

                //binding.starButton.starClickedUtil(!mobileLanguage.favorites)

                withContext(Dispatchers.IO){

                    mobileQueryRepository.updateMobileLanguageToStar(mobileLanguage)

                }
            }
        }

    }
}
