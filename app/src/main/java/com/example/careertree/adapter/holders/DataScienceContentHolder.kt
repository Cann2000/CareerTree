package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowDatascienceContentBinding
import com.example.careertree.repository.DataScienceQueryRepository
import com.example.careertree.utility.ClickListener
import com.example.careertree.view.data_science.DataScienceContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataScienceContentHolder(val binding: RecyclerRowDatascienceContentBinding, val dataScienceQueryRepository: DataScienceQueryRepository): RecyclerView.ViewHolder(binding.root)
    ,ClickListener {

    override fun dataClicked(view: View) {

        val dataScienceLanguage = binding.dataScienceLanguage

        if(dataScienceLanguage != null){

            val action = DataScienceContentFragmentDirections.actionDataScienceContentFragmentToDataScienceDetailsFragment(dataScienceLanguage.uuid!!)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Main).launch{

            val dataScienceLanguage = binding.dataScienceLanguage

            dataScienceLanguage?.let {

                //binding.starButton.starClickedUtil(!dataScienceLanguage.favorites)

                dataScienceQueryRepository.updateDataScienceLanguageToStar(dataScienceLanguage)
            }

        }    }

    override fun compareClicked(view: View) {
        TODO("Not yet implemented")
    }
}