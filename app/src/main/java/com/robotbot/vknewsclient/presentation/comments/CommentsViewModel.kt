package com.robotbot.vknewsclient.presentation.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.vknewsclient.data.repository.NewsFeedRepository
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.PostComment
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
) : ViewModel() {

    private val repository = NewsFeedRepository(application)

    val screenState = repository.getComments(feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }

}