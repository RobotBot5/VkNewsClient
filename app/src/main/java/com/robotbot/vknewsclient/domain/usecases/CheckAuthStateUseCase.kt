package com.robotbot.vknewsclient.domain.usecases

import com.robotbot.vknewsclient.domain.entity.AuthState
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.entity.PostComment
import com.robotbot.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class CheckAuthStateUseCase(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke() {
        repository.checkAuthState()
    }

}