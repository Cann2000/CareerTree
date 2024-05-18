package com.example.careertree.repository

import com.example.careertree.service.api.DataAPI
import com.example.careertree.utility.Resource
import java.lang.Exception
import javax.inject.Inject

class APIRepository @Inject constructor(private val dataAPI: DataAPI){

    suspend fun getDataVersionName(): Resource<String> {
        return try {

            val response = dataAPI.getMainData()

            if (response.isSuccessful) {

                val data = response.body()

                if (data != null) {

                    val version_name = data.version_name
                    return Resource.success(version_name)
                }
                else {
                    Resource.error("Error", null)
                }
            }
            else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error(e.localizedMessage, null)
        }
    }

    suspend fun getMainDataAPI(dataType: String): Resource<List<Any>> {
        return try {

            val response = dataAPI.getMainData()

            if (response.isSuccessful) {

                val data = response.body()

                if (data != null) {

                    val dataToReturn: List<Any>? = when (dataType) {

                        "blogs" -> data.blogs
                        "contents" -> data.contents
                        "events" -> data.events
                        "android" -> data.mobile.languages
                        "androidSalary" -> data.mobile.salary
                        "webBackend" -> data.web.backend.languages
                        "webFrontend" -> data.web.frontend.languages
                        "webSalary" -> data.web.salary
                        "gameEngine" -> data.game.languages
                        "gameEngineSalary" -> data.game.salary
                        "ai" -> data.ai.languages
                        "aiSalary" -> data.ai.salary
                        "cyberSecurity" -> data.cyber_security.languages
                        "cyberSecuritySalary" -> data.cyber_security.salary
                        "dataScience" -> data.data_science.languages
                        "dataScienceSalary" -> data.data_science.salary

                        else -> null // emptyList()
                    }

                    return Resource.success(dataToReturn)
                }
                else {
                    Resource.error("Error", null)
                }
            }
            else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error(e.localizedMessage, null)
        }
    }

    suspend fun getProfileTextDataAPI(dataType: String): Resource<List<String>> {

        return try {

            val response = dataAPI.getProfileTextData()

            if(response.isSuccessful){

                val data = response.body()

                if(data != null){

                    val dataToReturn: List<String>? = when(dataType){

                        "universities" -> data.universities.map { it.name }
                        "departments" -> data.departments.map { it.name }
                        "skills" -> data.skills.map { it.name }

                        else -> null
                    }
                    return Resource.success(dataToReturn)
                }
                else{
                    Resource.error("Error", null)
                }
            }
            else{
                Resource.error("Error", null)
            }
        }
        catch (e:Exception){
            Resource.error(e.localizedMessage, null)
        }
    }


}