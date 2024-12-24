package com.robotbot.vknewsclient.ui.theme

import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.PostComment

sealed interface HomeScreenState {

    data object Initial : HomeScreenState

    data class Posts(val posts: List<FeedPost>) : HomeScreenState

    data class Comments(val feedPost: FeedPost, val comments: List<PostComment>) : HomeScreenState

}