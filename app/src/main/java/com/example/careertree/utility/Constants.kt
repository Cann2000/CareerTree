package com.example.careertree.utility

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.careertree.model.profile.Profile
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object Constants {

    const val BASE_URL = "https://raw.githubusercontent.com/"
    const val SERVER_CLIENT_ID = "Gizli"
    const val Gemini_API = "Gizli"
    const val Gemini_ModelName = "Gizli"
    const val PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.canyldz.careertree"

    const val TIME = "time" // shared preferences

    var loadAndroid:Boolean = true
    var loadGame:Boolean = true
    var loadWebBackend:Boolean = true
    var loadWebFrontend:Boolean = true
    var loadAiLanguage:Boolean = true
    var loadCyberSecurityLanguage:Boolean = true
    var loadDataScienceLanguage:Boolean = true
    var loadHomePage:Boolean = true
    var loadEventList:Boolean = true

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestoreDB: FirebaseFirestore
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var firebaseAnalytics: FirebaseAnalytics


    var profile:Profile? = null


}