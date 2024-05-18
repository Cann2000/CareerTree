package com.example.careertree.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.adapter.PersonProfilesAdapter
import com.example.careertree.model.profile.Profile
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class PersonProfilesViewModel:ViewModel() {

    val personProfileList = MutableLiveData<Resource<List<Profile>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    val profileList = ArrayList<Profile>()

    private val execptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }


    fun getDataFromFirebase(){

        loading.value = Resource.loading(data = true)

        try {

            viewModelScope.launch(Dispatchers.IO + execptionHandler) {

                Constants.firestoreDB.collection("UserInformation").addSnapshotListener { snaphsot, error ->

                    if(error != null){
                        errorMessage.value = Resource.error(error.toString(),true)
                        return@addSnapshotListener
                    }
                    if(snaphsot != null && !snaphsot.isEmpty){

                        profileList.clear()

                        for (doc in snaphsot.documents){

                            val imageUrl = doc.getString("ImageUrl")
                            val username = doc.getString("UserName")
                            val school = doc.getString("School")
                            val universityDepartment = doc.getString("University_Department")
                            val skills = doc.getString("Skills")
                            val portfolio = doc.getString("Portfolio")

                            val profile = Profile(doc.id,username,school,universityDepartment,skills,portfolio,imageUrl)

                            profileList.add(profile)

                        }

                        showPersonProfiles(profileList)
                    }
                }
            }
        }
        catch (e:Exception){
            errorMessage.value = Resource.error(e.localizedMessage, data = true)
        }
    }


    fun searchViewFilterList(query:String?,adapter:PersonProfilesAdapter)
    {
        try {

            viewModelScope.launch(Dispatchers.IO + execptionHandler) {

                if (!query.isNullOrEmpty()) {

                    val filteredList = ArrayList<Profile>()

                    profileList.isNotEmpty().let {

                        profileList.forEach {

                            val profileName = it.name?.lowercase(Locale.ROOT)
                            val profileSkills = it.skills?.lowercase(Locale.ROOT)
                            val profileSchool = it.school?.lowercase(Locale.ROOT)

                            if (profileName?.startsWith(query.lowercase(Locale.ROOT)) == true
                                || profileSkills?.contains(query.lowercase(Locale.ROOT)) == true
                                || profileSchool?.contains(query.lowercase(Locale.ROOT)) == true) {

                                filteredList.add(it)
                            }
                        }

                        withContext(Dispatchers.Main) {
                            adapter.setFilteredList(filteredList)
                        }
                    }
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        adapter.dataListUpdate(profileList)
                    }
                }
            }
        }
        catch (e:Exception){
            errorMessage.value = Resource.error(e.localizedMessage, data = true)
        }
    }

    private fun showPersonProfiles(personProfile:List<Profile>){

        viewModelScope.launch {

            personProfileList.value = Resource.success(personProfile)
            errorMessage.value = Resource.error("successful", data = false)
            loading.value = Resource.loading(data = false)
        }
    }
}