package com.robotbot.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val _feedPost = MutableLiveData(FeedPost())
    val feedPost: LiveData<FeedPost> = _feedPost

    fun incrementStatisticItem(itemToIncrement: StatisticItem) {
        val oldStatistics = feedPost.value?.statistics ?: throw IllegalStateException()
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { item ->
                if (item.type == itemToIncrement.type) {
                    item.copy(count = item.count + 1)
                } else {
                    item
                }
            }
        }
        _feedPost.value = feedPost.value?.copy(statistics = newStatistics)
    }

}