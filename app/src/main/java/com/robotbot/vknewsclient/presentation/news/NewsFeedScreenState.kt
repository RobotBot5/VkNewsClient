package com.robotbot.vknewsclient.presentation.news

import com.robotbot.vknewsclient.domain.entity.FeedPost

sealed interface NewsFeedScreenState {

    data object Initial : NewsFeedScreenState

    data object Loading : NewsFeedScreenState

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState

}