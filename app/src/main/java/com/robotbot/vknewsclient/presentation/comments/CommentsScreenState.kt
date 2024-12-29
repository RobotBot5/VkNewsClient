package com.robotbot.vknewsclient.presentation.comments

import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.PostComment

sealed interface CommentsScreenState {

    data object Initial : CommentsScreenState

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState

}