package com.example.careertree.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.profile.Profile
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PersonProfilesDetailsViewModel:ViewModel() {

    val profileLiveData = MutableLiveData<Resource<Profile>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    fun getDataFromFirebase(token:String){

        loading.value = Resource.loading(data = true)

        viewModelScope.launch(Dispatchers.IO) {

            try {

                Constants.firestoreDB.collection("UserInformation").document(token).get().addOnSuccessListener { document ->

                    if(document != null){

                        val imageUrl = document.getString("ImageUrl")
                        val username = document.getString("UserName")
                        val school = document.getString("School")
                        val universityDepartment = document.getString("University_Department")
                        val skills = document.getString("Skills")
                        val portfolio = document.getString("Portfolio")

                        val profile = Profile(document.id,username,school,universityDepartment,skills,portfolio,imageUrl)
                        showProfile(profile)
                    }
                    else {
                        println("Belge bulunamadı.")
                    }

                }.addOnFailureListener { exception ->
                    // Hata durumunda buraya düşer
                    errorMessage.value = Resource.error(exception.localizedMessage, data = true)
                }
            }
            catch (e:Exception){
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
            }

        }
    }

    private fun showProfile(profile: Profile){

        viewModelScope.launch {

            profileLiveData.value = Resource.success(profile)
            errorMessage.value = Resource.error("successful", data = false)
            loading.value = Resource.loading(data = false)
        }
    }
}