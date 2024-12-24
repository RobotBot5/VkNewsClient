package com.robotbot.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.PostComment
import com.robotbot.vknewsclient.domain.StatisticItem
import com.robotbot.vknewsclient.ui.theme.HomeScreenState

class MainViewModel : ViewModel() {

    private val comments = mutableListOf<PostComment>().apply {
        repeat(10) {
            add(PostComment(it))
        }
    }

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(
                FeedPost(id = it)
            )
        }
    }

    private var savedState: HomeScreenState? = HomeScreenState.Initial

    private val _state = MutableLiveData<HomeScreenState>(HomeScreenState.Posts(initialList))
    val state: LiveData<HomeScreenState> = _state

    fun showComments(feedPost: FeedPost) {
        savedState = _state.value
        _state.value = HomeScreenState.Comments(feedPost = feedPost, comments = comments)
    }

    fun closeComments() {
        _state.value = savedState
    }

    fun incrementStatisticItem(post: FeedPost, itemToIncrement: StatisticItem) {
        val currentState = state.value
        if (currentState !is HomeScreenState.Posts) return
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
        _state.value = HomeScreenState.Posts(modifiedList)
    }

    fun deletePost(post: FeedPost) {
        val currentState = state.value
        if (currentState !is HomeScreenState.Posts) return

        val modifiedList = currentState.posts.toMutableList()
        modifiedList.remove(post)
        _state.value = HomeScreenState.Posts(modifiedList)
    }

}