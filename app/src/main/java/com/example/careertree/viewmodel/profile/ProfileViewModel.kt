package com.example.careertree.viewmodel.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.profile.Profile
import com.example.careertree.repository.APIRepository
import com.example.careertree.utility.Constants
import com.example.careertree.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(private val apiRepository: APIRepository) :ViewModel() {

    val universitiesLiveData = MutableLiveData<Resource<List<String>>>()
    val departmentsLiveData = MutableLiveData<Resource<List<String>>>()
    val skillsLiveData = MutableLiveData<Resource<List<String>>>()

    val profileLiveData = MutableLiveData<Resource<Profile>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()
    val dataLoaded = MutableLiveData<Resource<Boolean>>()

    var universityList = ArrayList<String>()
    var departmentList = ArrayList<String>()
    var skillList = ArrayList<String>()

    private val execptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->
        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)
    }

    fun saveBtn(name:String,school:String,universityDepartment:String,skills:String,portfolio:String,imagerUri: Uri?){

        saveDataToFirebase(name,school,universityDepartment,skills,portfolio,imagerUri)
    }

    fun loadProfileFromFirebase(){

        loading.value = Resource.loading(true)

        viewModelScope.launch(Dispatchers.IO + execptionHandler) {

            Constants.profile?.let { showProfile(it) } // Daha uygulamaya ilk giriş yapıldığında veri firebaseden çekilip Constants.profile'de tutulur. Daha sonra değiştirilebilir


            val universities = apiRepository.getProfileTextDataAPI("universities")
            val departments = apiRepository.getProfileTextDataAPI("departments")
            val skills = apiRepository.getProfileTextDataAPI("skills")

            universities.data?.let {

                universityList = ArrayList(it)
            }
            departments.data?.let {

                departmentList = ArrayList(it)
            }
            skills.data?.let {

                skillList = ArrayList(it)
            }

            showProfileTexts(universityList,departmentList,skillList)
        }
    }



    private fun saveDataToFirebase(name:String,school:String,universityDepartment:String,portfolio:String,skills:String,imagerUri: Uri?){

        dataLoaded.value = Resource.loading(false)
        loading.value = Resource.loading(true)


        viewModelScope.launch(Dispatchers.IO + execptionHandler) {

            try {

                val userInformationMap = HashMap<String, Any>()
                val userToken = Constants.firebaseAuth.currentUser!!.uid

                if (imagerUri != null) { // kullanici eger galeriden bir resim sectiyse

                    val uuid = UUID.randomUUID()
                    var imageUrl: String
                    val imageName = "$uuid.jpg"
                    val imageReference = Constants.firebaseStorage.reference.child("images").child(imageName)

                    imageReference.putFile(imagerUri).addOnSuccessListener {

                        imageReference.downloadUrl.addOnSuccessListener {

                            imageUrl = it.toString()

                            userInformationMap["ImageUrl"] = imageUrl
                            userInformationMap["UserMail"] = Constants.firebaseAuth.currentUser!!.email.toString()
                            userInformationMap["UserName"] = name
                            userInformationMap["School"] = school
                            userInformationMap["University_Department"] = universityDepartment
                            userInformationMap["Skills"] = skills
                            userInformationMap["Portfolio"] = portfolio

                            Constants.firestoreDB.collection("UserInformation").document(userToken)
                                .set(userInformationMap)
                                .addOnSuccessListener {
                                    Constants.profile = Profile(userToken,name,school,universityDepartment,skills,portfolio,imageUrl)
                                    checkDataLoaded()
                                }
                                .addOnFailureListener { e ->
                                    e.printStackTrace()

                                }
                        }
                    }
                } else {

                    Constants.profile?.imageUrl?.let { userInformationMap.put("ImageUrl", it) }
                    userInformationMap["UserMail"] = Constants.firebaseAuth.currentUser!!.email.toString()
                    userInformationMap["UserName"] = name
                    userInformationMap["School"] = school
                    userInformationMap["University_Department"] = universityDepartment
                    userInformationMap["Skills"] = skills
                    userInformationMap["Portfolio"] = portfolio


                    Constants.firestoreDB.collection("UserInformation").document(userToken).set(userInformationMap).addOnSuccessListener {
                        Constants.profile = Profile(userToken,name,school,universityDepartment,skills,portfolio,Constants.profile?.imageUrl)
                        checkDataLoaded()
                    }.addOnFailureListener { e ->
                            e.printStackTrace()
                        }
                }


            } catch (e: Exception) {
                errorMessage.value = Resource.error(e.localizedMessage, data = true)
                loading.value = Resource.loading(false)

            }
        }
    }

    private  fun checkDataLoaded(){
        dataLoaded.value = Resource.loading(true)
    }
    private fun showProfile(profile: Profile){

        viewModelScope.launch {

            profileLiveData.value = Resource.success(profile)
            errorMessage.value = Resource.error("successful",data = false)
            loading.value = Resource.loading(false)
        }
    }
    private fun showProfileTexts(
        universities: List<String>?,departments:List<String>?,skills:List<String>?){

        viewModelScope.launch {

            universitiesLiveData.value = Resource.success(universities)
            departmentsLiveData.value = Resource.success(departments)
            skillsLiveData.value = Resource.success(skills)
        }
    }
}