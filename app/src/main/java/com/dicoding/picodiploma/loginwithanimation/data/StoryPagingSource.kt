package com.dicoding.picodiploma.loginwithanimation.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem

class StoryPagingSource(
    private val repository: StoryRepository,
    private val accessToken: String
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val pageNumber = params.key ?: INITIAL_PAGE_INDEX
            val response = repository.getStories(accessToken, pageNumber)
            val stories = response.listStory.orEmpty().filterNotNull()

            LoadResult.Page(
                data = stories,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (stories.isEmpty()) null else pageNumber + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
