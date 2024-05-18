package com.example.careertree.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.careertree.utility.Constants
import javax.inject.Inject

class SharedPreferencesRepository@Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveTime(time:Long){

        sharedPreferences?.edit {
            putLong(Constants.TIME,time)
            commit()
        }
    }

    fun takeTime() = sharedPreferences?.getLong(Constants.TIME,0)

    fun clearSharedPreferences(){

        saveTime(0)
    }
}