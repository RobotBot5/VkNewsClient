package com.robotbot.vknewsclient.ui.theme

import com.robotbot.vknewsclient.domain.FeedPost

sealed interface NewsFeedScreenState {

    data object Initial : NewsFeedScreenState

    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState

}