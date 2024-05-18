package com.example.careertree.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharedPreferencesModule {

    @Singleton
    @Provides
    fun hiddenSharedPreferences(@ApplicationContext context: Context):SharedPreferences{

        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}