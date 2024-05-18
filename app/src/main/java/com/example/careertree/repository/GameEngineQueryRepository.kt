package com.example.careertree.repository

import android.view.View
import androidx.navigation.Navigation
import com.example.careertree.model.favorite.Favorite
import com.example.careertree.model.game.GameEngine
import com.example.careertree.service.dao.GameEngineDao
import com.example.careertree.view.game.GameEngineContentFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GameEngineQueryRepository @Inject constructor(
    private val gameEngineDao: GameEngineDao,
    private val favoriteQueryRepository: FavoriteQueryRepository) {

    suspend fun insertAllGameEngine(gameEngineList: List<GameEngine>){

        CoroutineScope(Dispatchers.IO).launch {

            gameEngineDao.deleteAll()

            val uuid = gameEngineDao.insertAll(*gameEngineList.toTypedArray())
            gameEngineList.forEachIndexed { index, gameEngine ->
                gameEngine.uuid = uuid[index].toInt()
            }

        }
    }
    suspend fun getGameEngine(uuid:Int):GameEngine{

        return withContext(Dispatchers.IO){
            gameEngineDao.getGameEngine(uuid)
        }
    }

    suspend fun getAllGameEngine():List<GameEngine>{

        return withContext(Dispatchers.IO){
            gameEngineDao.getAllGameEngine()
        }
    }

    suspend fun getComparedGameEngine(compared:Boolean):List<GameEngine>{

        return withContext(Dispatchers.IO){
            gameEngineDao.getComparedGameEngine(compared)
        }
    }

    suspend fun updateGameEngineToCompare(gameEngine: GameEngine,view:View){

        CoroutineScope(Dispatchers.IO).launch {

            if(!gameEngine.compared){

                gameEngine.compared = true
                gameEngineDao.updateGameEngine(gameEngine)

                val comparedGameEngine = gameEngineDao.getComparedGameEngine(true)

                if(comparedGameEngine.size == 2){

                    withContext(Dispatchers.Main){
                        delay(100)
                        val action = GameEngineContentFragmentDirections.actionGameEngineContentFragmentToCompareFragment("game")
                        Navigation.findNavController(view).navigate(action)
                    }
                }

            }
            else{

                gameEngine.compared = false
                gameEngineDao.updateGameEngine(gameEngine)
            }
        }
    }
    suspend fun updateGameEngineToStar(gameEngine: GameEngine){

        CoroutineScope(Dispatchers.IO).launch {

            if(!gameEngine.favorites){

                gameEngine.favorites = true
                gameEngineDao.updateGameEngine(gameEngine)

                val favorite = Favorite(gameEngine.name,gameEngine.imageUrl)
                favoriteQueryRepository.insertFavorite(favorite)
            }
            else{

                gameEngine.favorites = false
                gameEngineDao.updateGameEngine(gameEngine)

                favoriteQueryRepository.deleteFavorites(gameEngine.name)
            }
        }
    }
    suspend fun makeComparedFalse(comparedGameEngine:List<GameEngine>){

        CoroutineScope(Dispatchers.IO).launch{

            comparedGameEngine[0].compared = false
            comparedGameEngine[1].compared = false

            gameEngineDao.updateGameEngine(comparedGameEngine[0])
            gameEngineDao.updateGameEngine(comparedGameEngine[1])
        }
    }
}