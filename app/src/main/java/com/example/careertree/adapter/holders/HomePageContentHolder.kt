package com.example.careertree.adapter.holders

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowHomepageBinding
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.Constants
import com.example.careertree.view.homepage.HomePageFragmentDirections
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePageContentHolder(val binding: RecyclerRowHomepageBinding): RecyclerView.ViewHolder(binding.root),
    ClickListener {

    override fun dataClicked(view: View) {

        val content = binding.content

        val action = findFragmentTagForContent(content?.contentName)

        CoroutineScope(Dispatchers.IO).launch{

            Constants.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.ITEM_NAME, content?.contentName.toString())

            }
        }

        action?.let {

            Navigation.findNavController(view).navigate(action)
        }

    }

    override fun compareClicked(view: View) {
        TODO("Not yet implemented")
    }

    override fun starClicked(view: View) {
        TODO("Not yet implemented")
    }

    private fun findFragmentTagForContent(contentName:String?): NavDirections? {
        return when (contentName) {
            "Mobil Geliştiriciliği" -> HomePageFragmentDirections.actionHomePageFragmentToMobileContentFragment()
            "Web Geliştiriciliği" -> HomePageFragmentDirections.actionHomePageFragmentToWebContentFragment()
            "Oyun Geliştiriciliği" -> HomePageFragmentDirections.actionHomePageFragmentToGameEngineContentFragment()
            "Yapay Zeka Geliştiriciliği" -> HomePageFragmentDirections.actionHomePageFragmentToAiContentFragment()
            "Siber Güvenlik" -> HomePageFragmentDirections.actionHomePageFragmentToCyberSecurityContentFragment()
            "Veri Bilimi" -> HomePageFragmentDirections.actionHomePageFragmentToDataScienceContentFragment()
            // Diğer fragmentlere geçişleri burada ekle
            else -> null
        }
    }
}
