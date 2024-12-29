package com.robotbot.vknewsclient.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.vknewsclient.data.mapper.NewsFeedMapper
import com.robotbot.vknewsclient.data.network.ApiFactory
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.StatisticItem
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val state: LiveData<NewsFeedScreenState> = _state

    private val mapper = NewsFeedMapper()

    init {
        loadWall()
    }

    private fun loadWall() {
        viewModelScope.launch {
            val storage = VKPreferencesKeyValueStorage(getApplication())
            val token = VKAccessToken.restore(storage) ?: return@launch
            val response = ApiFactory.apiService.loadWall(token.accessToken)
            val feedPosts = mapper.mapResponseToPost(response)
            _state.value = NewsFeedScreenState.Posts(posts = feedPosts)
        }
    }

    fun incrementStatisticItem(post: FeedPost, itemToIncrement: StatisticItem) {
        val currentState = state.value
        if (currentState !is NewsFeedScreenState.Posts) return
        val modifiedList = currentState.posts.toMutableList()
        modifiedList.replaceAll { oldPost ->
            if (post == oldPost) {
                val oldStatistics = oldPost.statistics
                val newStatistics = oldStatistics.toMutableList().apply {
                    replaceAll { item ->
                        if (item.type == itemToIncrement.type) {
                            item.copy(count = item.count + 1)
                        } else {
                            item
                        }
                    }
                }
                oldPost.copy(statistics = newStatistics)
            } else {
                oldPost
            }
        }
        _state.value = NewsFeedScreenState.Posts(modifiedList)
    }

    fun deletePost(post: FeedPost) {
        val currentState = state.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val modifiedList = currentState.posts.toMutableList()
        modifiedList.remove(post)
        _state.value = NewsFeedScreenState.Posts(modifiedList)
    }

}