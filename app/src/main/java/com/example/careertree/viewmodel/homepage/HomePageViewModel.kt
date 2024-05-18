package com.example.careertree.viewmodel.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careertree.model.blog.Blog
import com.example.careertree.repository.APIRepository
import com.example.careertree.utility.Resource
import com.example.careertree.model.homepage.HomePageContent
import com.example.careertree.utility.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val apiRepository: APIRepository):ViewModel() {

    val version_name = MutableLiveData<Resource<String>>()
    val contentList = MutableLiveData<Resource<List<HomePageContent>>>()
    val blogList = MutableLiveData<Resource<List<Blog>>>()
    val errorMessage = MutableLiveData<Resource<Boolean>>()
    val loading = MutableLiveData<Resource<Boolean>>()

    private val execptionHandler= CoroutineExceptionHandler { coroutineContext, throwable ->

        errorMessage.value = Resource.error(throwable.localizedMessage, data = true)

        println("Error: " + throwable.localizedMessage)
    }

    fun getData(){

        if(Constants.loadHomePage){

            getDataFromInternet()
        }
    }

    private fun getDataFromInternet() {

        loading.value = Resource.loading(data = true)

        viewModelScope.launch(Dispatchers.IO + execptionHandler) {

            try {

                val versionName = apiRepository.getDataVersionName()

                versionName.data?.let {

                    viewModelScope.launch {

                        version_name.value = Resource.success(it)
                    }
                }

                val contents = apiRepository.getMainDataAPI("contents")

                contents.data?.let {

                    showContent(it.map { it as HomePageContent })
                }

                val blogs = apiRepository.getMainDataAPI("blogs")

                blogs.data?.let {

                    showBlog(it.map { it as Blog })
                }

            } catch (e: Exception) {

                viewModelScope.launch {

                    errorMessage.value = Resource.error("successful", data = false)

                }
            }
        }
    }

    private fun showContent(content: List<HomePageContent>){

        viewModelScope.launch {

            contentList.value = Resource.success(content)
            loading.value = Resource.loading(data = false)
            errorMessage.value = Resource.error("successful", data = false)

            Constants.loadHomePage = false

        }
    }

    private fun showBlog(blog: List<Blog>){

        viewModelScope.launch {

            blogList.value = Resource.success(blog)
        }
    }
}