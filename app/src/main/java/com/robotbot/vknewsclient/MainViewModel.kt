package com.robotbot.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.StatisticItem
import com.robotbot.vknewsclient.ui.theme.NavigationItem

class MainViewModel : ViewModel() {

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(
                FeedPost(id = it)
            )
        }
    }

    private val _feedPosts = MutableLiveData<List<FeedPost>>(initialList)
    val feedPosts: LiveData<List<FeedPost>> = _feedPosts

    private val _selectedNavItem = MutableLiveData<NavigationItem>(NavigationItem.Home)
    val selectedNavItem: LiveData<NavigationItem> = _selectedNavItem

    fun selectNavItem(item: NavigationItem) {
        _selectedNavItem.value = item
    }

    fun incrementStatisticItem(post: FeedPost, itemToIncrement: StatisticItem) {
        val modifiedList = _feedPosts.value?.toMutableList() ?: mutableListOf()
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
        _feedPosts.value = modifiedList
    }

    fun deletePost(post: FeedPost) {
        val modifiedList = _feedPosts.value?.toMutableList() ?: mutableListOf()
        modifiedList.remove(post)
        _feedPosts.value = modifiedList
    }

}