package com.example.careertree.viewmodel.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.game.GameEngine
import com.example.careertree.repository.GameEngineQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class GameEngineDetailsViewModel @Inject constructor(private val gameEngineQueryRepository: GameEngineQueryRepository):ViewModel() {

    val gameEngineLiveData = MutableLiveData<Resource<GameEngine>>()

    fun getRoomData(uuid:Int){

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val gameEngine = gameEngineQueryRepository.getGameEngine(uuid)
                showGameEngine(gameEngine)
            }
            catch (e:Exception){
                println(e.localizedMessage)
            }
        }
    }

    private fun showGameEngine(gameEngine: GameEngine){

        viewModelScope.launch {

            gameEngineLiveData.value = Resource.success(gameEngine)
        }
    }
}