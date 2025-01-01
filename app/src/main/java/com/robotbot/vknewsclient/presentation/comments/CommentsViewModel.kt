package com.robotbot.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import com.robotbot.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
) : ViewModel() {

    private val repository = NewsFeedRepositoryImpl(application)

    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }

}