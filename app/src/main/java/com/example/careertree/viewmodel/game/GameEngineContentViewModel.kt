package com.example.careertree.viewmodel.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.salary.Salary
import com.example.careertree.model.game.GameEngine
import com.example.careertree.repository.APIRepository
import com.example.careertree.repository.GameEngineQueryRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameEngineContentViewModel @Inject constructor(private val apiRepository: APIRepository,private val gameEngineQueryRepository: GameEngineQueryRepository):ViewModel() {

    val salaryList = MutableLiveData<Resource<List<Salary>>>()
    val gameEngineList = MutableLiveData<Resource<List<GameEngine>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun getData(){

        if(Constants.loadGame){

            getDataFromInternet()
        }
        else{

            getDataFromSql()
        }
    }
    private fun getDataFromInternet(){

        loading.value = Resource.loading(data = true)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {
                val gameEngine = apiRepository.getMainDataAPI("gameEngine")

                gameEngine.data?.let {

                    saveToSql(it.map { it as GameEngine }).apply {

                        Constants.loadGame = false

                        showGameEngine(it.map { it as GameEngine })
                    }
                }

                val salary = apiRepository.getMainDataAPI("gameEngineSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })

                }

            }
            catch (e:Exception){
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }
    }

    private fun getDataFromSql(){

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            try {

                val gameEngine = gameEngineQueryRepository.getAllGameEngine()

                gameEngine.isNotEmpty().let {

                    showGameEngine(gameEngine)
                }

                val salary = apiRepository.getMainDataAPI("gameEngineSalary")

                salary.data?.let {

                    showSalary(it.map { it as Salary })
                }

            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }
        }

    }

    private suspend fun saveToSql(gameEngineList:List<GameEngine>){

        gameEngineQueryRepository.insertAllGameEngine(gameEngineList)

    }

    private fun showGameEngine(gameEngine: List<GameEngine>){

        viewModelScope.launch {

            gameEngineList.value = Resource.success(gameEngine)
            loading.value = Resource.loading(data = false)
            errorMessage.value = Resource.error("successful",data = false)
        }
    }
    private fun showSalary(salary: List<Salary>){

        viewModelScope.launch {

            salaryList.value = Resource.success(salary)
        }
    }
}