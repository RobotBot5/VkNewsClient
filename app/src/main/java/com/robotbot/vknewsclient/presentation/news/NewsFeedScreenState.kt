package com.robotbot.vknewsclient.presentation.news

import com.robotbot.vknewsclient.domain.FeedPost

sealed interface NewsFeedScreenState {

    data object Initial : NewsFeedScreenState

    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState

}