package com.example.careertree.service.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.careertree.model.event.Event
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.model.ai.AiLanguage
import com.example.careertree.model.mobile.MobileLanguage
import com.example.careertree.model.cyber_security.CyberSecurityLanguage
import com.example.careertree.model.data_science.DataScienceLanguage
import com.example.careertree.model.game.GameEngine
import com.example.careertree.model.web.WebBackendLanguage
import com.example.careertree.model.web.WebFrontendLanguage
import com.example.careertree.service.converter.Converter
import com.example.careertree.service.dao.AiDao
import com.example.careertree.service.dao.CyberSecurityDao
import com.example.careertree.service.dao.DataScienceDao
import com.example.careertree.service.dao.EventDao
import com.example.careertree.service.dao.FavoriteDao
import com.example.careertree.service.dao.GameEngineDao
import com.example.careertree.service.dao.MobileDao
import com.example.careertree.service.dao.WebDao

@Database(entities = [MobileLanguage::class,
    WebBackendLanguage::class,
    WebFrontendLanguage::class,
    GameEngine::class,
    AiLanguage::class,
    CyberSecurityLanguage::class,
    DataScienceLanguage::class,
    Favorite::class,
    Event::class,
    ], version = 1)

@TypeConverters(Converter::class)
abstract class Room_Database : RoomDatabase() {
    abstract fun mobileDao(): MobileDao
    abstract fun webDao(): WebDao
    abstract fun gameEnginDao(): GameEngineDao
    abstract fun aiDao(): AiDao
    abstract fun cyberSecurityDao(): CyberSecurityDao
    abstract fun dataScienceDao(): DataScienceDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun eventDao(): EventDao
}