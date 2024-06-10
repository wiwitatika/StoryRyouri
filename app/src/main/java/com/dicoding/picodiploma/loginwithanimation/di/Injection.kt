package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiConfig

object Injection {
    private var userRepository: UserRepository? = null
    private var storyRepository: StoryRepository? = null

    fun provideUserRepository(context: Context): UserRepository {
        if (userRepository == null) {
            userRepository = UserRepository.getInstance(UserPreference.getInstance(context.dataStore), ApiConfig.getApiService(""))
        }
        return userRepository!!
    }

    fun provideStoryRepository(): StoryRepository {
        if (storyRepository == null) {
            storyRepository = StoryRepository()
        }
        return storyRepository!!
    }
}