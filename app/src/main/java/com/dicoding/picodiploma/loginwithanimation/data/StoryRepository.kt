package com.dicoding.picodiploma.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepository {

    fun createPager(authToken: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(this, authToken) }
        ).liveData
    }

    suspend fun getStories(token: String, nextPageNumber: Int): StoryResponse {
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.getStories(nextPageNumber)
    }

    suspend fun uploadImage(
        imageFile: File,
        description: String,
        token: String,
        lat: Float?,
        lon: Float?
    ): FileUploadResponse {
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val imageBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("photo", imageFile.name, imageBody)
        val latPart = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lonPart = lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val apiService = ApiConfig.getApiService(token)
        return apiService.uploadImage(imagePart, descriptionBody, latPart, lonPart)
    }

    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        val apiServiceWithToken = ApiConfig.getApiService(token)
        return apiServiceWithToken.getStoriesWithLocation(1)
    }

}
