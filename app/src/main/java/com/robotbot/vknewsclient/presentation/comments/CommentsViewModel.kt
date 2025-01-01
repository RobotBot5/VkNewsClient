package com.robotbot.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import com.robotbot.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }

}