package com.robotbot.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.robotbot.vknewsclient.domain.usecases.DeletePostUseCase
import com.robotbot.vknewsclient.domain.usecases.GetWallUseCase
import com.robotbot.vknewsclient.domain.usecases.LoadNextDataUseCase
import com.robotbot.vknewsclient.extentions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    getWallUseCase: GetWallUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exception handler")
    }

    private val wallFlow = getWallUseCase()

    private val loadNextDataEvents = MutableSharedFlow<Unit>()
    private val loadNextDataFlow = flow {
        loadNextDataEvents.collect {
            emit(
                NewsFeedScreenState.Posts(
                    posts = wallFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }

    val state = wallFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextWall() {
        viewModelScope.launch {
            loadNextDataEvents.emit(Unit)
            loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun deletePost(post: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            deletePostUseCase(post)
        }
    }

}