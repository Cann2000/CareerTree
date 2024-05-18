package com.example.careertree.viewmodel.compare

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.compare.Compare
import com.example.careertree.repository.AiQueryRepository
import com.example.careertree.repository.CyberSecurityQueryRepository
import com.example.careertree.repository.GameEngineQueryRepository
import com.example.careertree.repository.MobileQueryRepository
import com.example.careertree.repository.WebQueryRepository
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CompareViewModel@Inject constructor(
    private val mobileQueryRepository: MobileQueryRepository,
    private val gameEngineQueryRepository: GameEngineQueryRepository,
    private val webQueryRepository: WebQueryRepository,
    private val aiQueryRepository: AiQueryRepository,
    private val cyberSecurityQueryRepository: CyberSecurityQueryRepository) :ViewModel() {

    val compareLiveData = MutableLiveData<Resource<List<Compare>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    fun getCompareData(key:String){

        loading.value = Resource.loading(data = true)

        when(key){

            "android" -> {

                viewModelScope.launch(Dispatchers.IO) {

                    val androidLanguage = mobileQueryRepository.getComparedLanguage(true)

                    if(androidLanguage.size == 2){ // veri sayım zaten 2 olmazsa bu compare fragmente girilmeyecek

                        val comparedData1  = Compare(androidLanguage[0].name,androidLanguage[0].advantage,androidLanguage[0].disadvantage,androidLanguage[0].imageUrl)
                        val comparedData2  = Compare(androidLanguage[1].name,androidLanguage[1].advantage,androidLanguage[1].disadvantage,androidLanguage[1].imageUrl)

                        val comparedDataList = ArrayList<Compare>()

                        comparedDataList.add(comparedData1)
                        comparedDataList.add(comparedData2)

                        mobileQueryRepository.makeComparedFalse(androidLanguage)

                        withContext(Dispatchers.Main){

                            compareLiveData.value = Resource.success(comparedDataList)
                            loading.value = Resource.loading(data = false)

                        }
                    }
                }
            }
            "web" -> {

                viewModelScope.launch(Dispatchers.IO) {

                    val webBackendLanguage = webQueryRepository.getComparedWebBackendLanguage(true)

                    if(webBackendLanguage.size == 2){ // veri sayım zaten 2 olmazsa bu compare fragmente girilmeyece

                        val comparedData1  = Compare(webBackendLanguage[0].name,webBackendLanguage[0].advantage,webBackendLanguage[0].disadvantage,webBackendLanguage[0].imageUrl)
                        val comparedData2  = Compare(webBackendLanguage[1].name,webBackendLanguage[1].advantage,webBackendLanguage[1].disadvantage,webBackendLanguage[1].imageUrl)

                        val comparedDataList = ArrayList<Compare>()

                        comparedDataList.add(comparedData1)
                        comparedDataList.add(comparedData2)

                        webQueryRepository.makeComparedFalse(webBackendLanguage)

                        withContext(Dispatchers.Main){

                            compareLiveData.value = Resource.success(comparedDataList)
                            loading.value = Resource.loading(data = false)

                        }
                    }
                }
            }
            "game" -> {

                viewModelScope.launch(Dispatchers.IO) {

                    val gameEngine = gameEngineQueryRepository.getComparedGameEngine(true)

                    if(gameEngine.size == 2){ // veri sayım zaten 2 olmazsa bu compare fragmente girilmeyece

                        val comparedData1  = Compare(gameEngine[0].name,gameEngine[0].advantage,gameEngine[0].disadvantage,gameEngine[0].imageUrl)
                        val comparedData2  = Compare(gameEngine[1].name,gameEngine[1].advantage,gameEngine[1].disadvantage,gameEngine[1].imageUrl)

                        val comparedDataList = ArrayList<Compare>()

                        comparedDataList.add(comparedData1)
                        comparedDataList.add(comparedData2)

                        gameEngineQueryRepository.makeComparedFalse(gameEngine)

                        withContext(Dispatchers.Main){

                            compareLiveData.value = Resource.success(comparedDataList)
                            loading.value = Resource.loading(data = false)

                        }
                    }
                }
            }
            "ai" -> {

                viewModelScope.launch(Dispatchers.IO) {

                    val aiLanguage = aiQueryRepository.getComparedAiLanguage(true)

                    if(aiLanguage.size == 2){ // veri sayım zaten 2 olmazsa bu compare fragmente girilmeyece

                        val comparedData1  = Compare(aiLanguage[0].name,aiLanguage[0].advantage,aiLanguage[0].disadvantage,aiLanguage[0].imageUrl)
                        val comparedData2  = Compare(aiLanguage[1].name,aiLanguage[1].advantage,aiLanguage[1].disadvantage,aiLanguage[1].imageUrl)

                        val comparedDataList = ArrayList<Compare>()

                        comparedDataList.add(comparedData1)
                        comparedDataList.add(comparedData2)

                        aiQueryRepository.makeComparedFalse(aiLanguage)

                        withContext(Dispatchers.Main){

                            compareLiveData.value = Resource.success(comparedDataList)
                            loading.value = Resource.loading(data = false)

                        }
                    }
                }
            }
            "cyber_security" -> {

                viewModelScope.launch(Dispatchers.IO) {

                    val cyberSecurityLanguage = cyberSecurityQueryRepository.getComparedCyberSecurityLanguage(true)

                    if(cyberSecurityLanguage.size == 2){ // veri sayım zaten 2 olmazsa bu compare fragmente girilmeyece

                        val comparedData1  = Compare(cyberSecurityLanguage[0].name,cyberSecurityLanguage[0].advantage,cyberSecurityLanguage[0].disadvantage,cyberSecurityLanguage[0].imageUrl)
                        val comparedData2  = Compare(cyberSecurityLanguage[1].name,cyberSecurityLanguage[1].advantage,cyberSecurityLanguage[1].disadvantage,cyberSecurityLanguage[1].imageUrl)

                        val comparedDataList = ArrayList<Compare>()

                        comparedDataList.add(comparedData1)
                        comparedDataList.add(comparedData2)

                        cyberSecurityQueryRepository.makeComparedFalse(cyberSecurityLanguage)

                        withContext(Dispatchers.Main){

                            compareLiveData.value = Resource.success(comparedDataList)
                            loading.value = Resource.loading(data = false)

                        }
                    }
                }
            }
        }

    }

}