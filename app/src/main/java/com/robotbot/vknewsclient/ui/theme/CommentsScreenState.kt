package com.robotbot.vknewsclient.ui.theme

import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.PostComment

sealed interface CommentsScreenState {

    data object Initial : CommentsScreenState

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState

}