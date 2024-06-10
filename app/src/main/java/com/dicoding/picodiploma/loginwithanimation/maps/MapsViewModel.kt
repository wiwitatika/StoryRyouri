package com.dicoding.picodiploma.loginwithanimation.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    private val _withLocation = MutableLiveData<List<ListStoryItem>>()
    val withLocation: LiveData<List<ListStoryItem>> get() = _withLocation

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> get() = _errorMsg

    fun fetchStoriesWithLocation() {
        _isProgressBarVisible.value = true
        viewModelScope.launch {
            try {
                val userSession = userRepository.getSession().first()
                val token = userSession!!.token
                val response = storyRepository.getStoriesWithLocation(token)
                if (response.error == true) {
                    _errorMsg.value = response.message
                } else {
                    _withLocation.value = response.listStory.orEmpty().filterNotNull()
                }
            } catch (e: Exception) {
                _errorMsg.value = "${e.message}"
            } finally {
                _isProgressBarVisible.value = false
            }
        }
    }

}