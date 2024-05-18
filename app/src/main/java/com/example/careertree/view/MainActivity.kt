package com.example.careertree.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import com.example.careertree.R
import com.example.careertree.databinding.ActivityMainBinding
import com.example.careertree.utility.Constants
import com.example.careertree.utility.closeNavigationDrawer
import com.example.careertree.utility.downloadImage
import com.example.careertree.utility.isNetworkAvailable
import com.example.careertree.utility.makePlaceHolder
import com.example.careertree.utility.uploadImage
import com.example.careertree.view.homepage.HomePageFragmentDirections
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var toggle: ActionBarDrawerToggle

    lateinit var navViewPersonName:TextView
    lateinit var navViewPersonImage:CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Constants.firebaseAuth = FirebaseAuth.getInstance()
        Constants.firestoreDB = FirebaseFirestore.getInstance()
        Constants.firebaseStorage = FirebaseStorage.getInstance()
        Constants.firebaseAnalytics = Firebase.analytics

        navViewPersonName = binding.navView.getHeaderView(0).findViewById(R.id.personName)
        navViewPersonImage = binding.navView.getHeaderView(0).findViewById(R.id.personImageView)

        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open_naw,R.string.close_naw)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        closeNavigationDrawer(this as AppCompatActivity)

        binding.navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_profile -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    val action = HomePageFragmentDirections.actionHomePageFragmentToProfileFragment()
                    Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)
                }
                /*
                R.id.nav_favorite -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    val action = HomePageFragmentDirections.actionHomePageFragmentToFavoriteListFragment()
                    Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)

                }

                 */
                R.id.nav_search -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    val action = HomePageFragmentDirections.actionHomePageFragmentToPersonProfilesFragment()
                    Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)
                }
                R.id.nav_events -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    val action = HomePageFragmentDirections.actionHomePageFragmentToEventListFragment()
                    Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)
                }
                R.id.nav_followed_events -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    val action = HomePageFragmentDirections.actionHomePageFragmentToFollowedEventListFragment()
                    Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)
                }
                R.id.nav_aiChatBot -> {

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    val action = HomePageFragmentDirections.actionHomePageFragmentToAiChatBotFragment()
                    Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)
                }

            }

            binding.drawerLayout.isClickable = false

            CoroutineScope(Dispatchers.IO).launch {
                Constants.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
                    param(FirebaseAnalytics.Param.ITEM_NAME,it.toString())
                }
            }

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){

            if(Constants.profile!= null){

                apply {
                    navViewPersonName.text = Constants.profile!!.name
                    downloadImage(navViewPersonImage,Constants.profile!!.imageUrl)
                }
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean { // navigation view için
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (ev.x < 50) { // açılış mesafesini uygun bir değerle değiştirilebilir
                return false
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}