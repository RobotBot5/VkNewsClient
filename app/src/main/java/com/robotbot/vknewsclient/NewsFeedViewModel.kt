package com.robotbot.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.StatisticItem
import com.robotbot.vknewsclient.ui.theme.NewsFeedScreenState

class NewsFeedViewModel : ViewModel() {

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(
                FeedPost(id = it)
            )
        }
    }

    private val _state = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Posts(initialList))
    val state: LiveData<NewsFeedScreenState> = _state

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