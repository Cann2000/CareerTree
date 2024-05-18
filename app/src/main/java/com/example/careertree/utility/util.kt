package com.example.careertree.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.careertree.R
import com.example.careertree.model.profile.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.MalformedURLException

fun ImageView.uploadImage(url:String?,placeholder: CircularProgressDrawable){

    if (!url.isNullOrEmpty()) {
        val options = RequestOptions.placeholderOf(placeholder)
        Glide.with(this).setDefaultRequestOptions(options).load(url).into(this)
    }
}

fun makePlaceHolder(context:Context):CircularProgressDrawable{

    return CircularProgressDrawable(context).apply {

        strokeWidth = 8f
        centerRadius = 20f
        start()
    }
}

@BindingAdapter("android:downloadImage")
fun downloadImage(view: ImageView,url: String?){

    view.uploadImage(url, makePlaceHolder(view.context))
}

@BindingAdapter("android:comparedState")
fun ImageView.compareClickedUtil(comparedState:Boolean){

    CoroutineScope(Dispatchers.Main).launch {

        if (!comparedState) {
            this@compareClickedUtil.setImageResource(R.drawable.baseline_compare_arrows_24)

        } else {
            this@compareClickedUtil.setImageResource(R.drawable.baseline_compare_arrows_24_blue)
        }
    }
}
@BindingAdapter("android:eventClickState")
fun ImageView.eventFollowClickedUtil(eventState:Boolean){

    CoroutineScope(Dispatchers.Main).launch {

        if (!eventState) {
            this@eventFollowClickedUtil.setImageResource(R.drawable.event_follow_not_clicked)

        } else {
            this@eventFollowClickedUtil.setImageResource(R.drawable.event_follow_clicked)
        }
    }
}
@BindingAdapter("android:starState")
fun ImageView.starClickedUtil(starredState:Boolean){

    CoroutineScope(Dispatchers.Main).launch {

        if (starredState) {
            this@starClickedUtil.setImageResource(R.drawable.starred)

        } else {
            this@starClickedUtil.setImageResource(R.drawable.not_starred)
        }
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val service = Context.CONNECTIVITY_SERVICE
    val manager = context.getSystemService(service) as ConnectivityManager?
    val network = manager?.activeNetworkInfo

    return (network != null)
}

fun isLinkValid(link: String): Boolean {
    return try {
        URLUtil.isValidUrl(link) && Patterns.WEB_URL.matcher(link).matches()
    } catch (e: MalformedURLException) {
        false
    }
}


fun openNavigationDrawer(activity: AppCompatActivity) {

    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

}
fun closeNavigationDrawer(activity: AppCompatActivity) {

    activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

}

fun setupAuthenticationEditTextListeners(editTextEmailAddress: EditText?, editTextPassword: EditText?){

    editTextEmailAddress?.addTextChangedListener(object: TextWatcher { //TextWatcher, kullanıcı tarafından EditText alanındaki metin değiştirildiğinde çağrılan bir arayüzdür ve metin değişikliklerini izlemek ve bunlara yanıt vermek için kullanılır.
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) { //Editable, Android platformunda metin alanlarında (örneğin, EditText alanları) kullanılan bir arayüzdür. Metni düzenleme ve metin alanlarında yapılan değişiklikleri temsil etmek için kullanılır.
            if(s != null && s.contains(" ")){
                val newText = s.toString().replace(" ", "")
                editTextEmailAddress.setText(newText)
                editTextEmailAddress.setSelection(newText.length) // EditText alanının içeriğini değiştirdikten sonra, imlecin konumunu belirler
            }
        }
    })

    editTextPassword?.addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if(s!= null && s.contains(" ")){
                val newText = s.toString().replace(" ","")
                editTextPassword.setText(newText)
                editTextPassword.setSelection(newText.length)
            }
        }

    })
}

fun closeInputFromWindow(view: View, context: Context){
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun loadPersonToFirebase(){

    CoroutineScope(Dispatchers.IO).launch {
        try {

            val userToken = Constants.firebaseAuth.currentUser!!.uid

            Constants.firestoreDB.collection("UserInformation").document(userToken).get().addOnSuccessListener { document ->

                if(document != null && document.exists()){

                    val imageUrl = document.getString("ImageUrl")
                    val username = document.getString("UserName")
                    val school = document.getString("School")
                    val universityDepartment = document.getString("University_Department")
                    val skills = document.getString("Skills")
                    val portfolio = document.getString("Portfolio")

                    Constants.profile = Profile(document.id,username,school,universityDepartment,skills,portfolio,imageUrl)

                }
                else {

                    Constants.profile = Profile(document.id,"","","","","","")
                }

            }.addOnFailureListener { exception ->
                // Hata durumunda buraya düşer

            }

        }
        catch (e: java.lang.Exception){
            println(e.localizedMessage)
        }

    }
}