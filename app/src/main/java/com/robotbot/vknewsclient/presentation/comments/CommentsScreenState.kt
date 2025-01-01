package com.robotbot.vknewsclient.presentation.comments

import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.entity.PostComment

sealed interface CommentsScreenState {

    data object Initial : CommentsScreenState

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState

}