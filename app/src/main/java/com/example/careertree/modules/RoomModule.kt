package com.example.careertree.modules

import android.content.Context
import androidx.room.Room
import com.example.careertree.service.database.Room_Database
import com.example.careertree.service.dao.AiDao
import com.example.careertree.service.dao.CyberSecurityDao
import com.example.careertree.service.dao.DataScienceDao
import com.example.careertree.service.dao.EventDao
import com.example.careertree.service.dao.FavoriteDao
import com.example.careertree.service.dao.GameEngineDao
import com.example.careertree.service.dao.MobileDao
import com.example.careertree.service.dao.WebDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext context: Context): Room_Database {

        return Room.databaseBuilder(context, Room_Database::class.java,"Database").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun getAndroidDao(database: Room_Database):MobileDao{

        return database.mobileDao()
    }

    @Singleton
    @Provides
    fun getWebDao(database: Room_Database):WebDao{

        return database.webDao()
    }

    @Singleton
    @Provides
    fun getGameEngineDao(database: Room_Database):GameEngineDao{

        return database.gameEnginDao()
    }

    @Singleton
    @Provides
    fun getAiDao(database: Room_Database):AiDao{

        return database.aiDao()
    }

    @Singleton
    @Provides
    fun getCyberSecurityDao(database: Room_Database):CyberSecurityDao{

        return database.cyberSecurityDao()
    }


    @Singleton
    @Provides
    fun getDataScienceDao(database: Room_Database):DataScienceDao{

        return database.dataScienceDao()
    }

    @Singleton
    @Provides
    fun getFavoriteDao(database: Room_Database):FavoriteDao{

        return database.favoriteDao()
    }

    @Singleton
    @Provides
    fun getEventDao(database: Room_Database):EventDao{

        return database.eventDao()
    }
}