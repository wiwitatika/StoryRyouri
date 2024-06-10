package com.dicoding.picodiploma.loginwithanimation.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    private val _shouldNavigateToMain = MutableLiveData<Boolean>()
    val shouldNavigateToMain: LiveData<Boolean> get() = _shouldNavigateToMain

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> get() = _errorMsg

    private val _includeLocation = MutableLiveData<Boolean>()

    private var userLatitude: Float? = null
    private var userLongitude: Float? = null

    fun includeLocation(enabled: Boolean) {
        _includeLocation.value = enabled
    }

    fun userLocation(latitude: Float, longitude: Float) {
        userLatitude = latitude
        userLongitude = longitude
    }


    fun uploadImage(imageFile: File, description: String) {
        viewModelScope.launch {
            try {
                _isProgressBarVisible.value = true
                val lat = if (_includeLocation.value == true) userLatitude else null
                val lon = if (_includeLocation.value == true) userLongitude else null
                val userSession = userRepository.getSession().first()
                val token = userSession!!.token
                storyRepository.uploadImage(imageFile, description, token, lat, lon)
                _errorMsg.value = "Image uploaded successfully"
                _shouldNavigateToMain.value = true
            } catch (e: Exception) {
                _errorMsg.value = "An error occurred while uploading the image: ${e.message}"
            } finally {
                _isProgressBarVisible.value = false
            }
        }
    }
}