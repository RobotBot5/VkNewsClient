package com.robotbot.vknewsclient.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.robotbot.vknewsclient.data.repository.NewsFeedRepository
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.StatisticItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val state: LiveData<NewsFeedScreenState> = _state

    private val repository = NewsFeedRepository(application)

    init {
        loadWall()
    }

    private fun loadWall() {
        viewModelScope.launch {
            val feedPosts = repository.loadWall()
            _state.value = NewsFeedScreenState.Posts(posts = feedPosts)
        }
    }

    fun loadNextWall() {
        _state.value = NewsFeedScreenState.Posts(
            posts = repository.feedPosts,
            nextDataIsLoading = true
        )
        loadWall()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
            _state.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
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