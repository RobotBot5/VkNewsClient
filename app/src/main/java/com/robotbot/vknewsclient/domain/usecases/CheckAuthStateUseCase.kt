package com.robotbot.vknewsclient.domain.usecases

import com.robotbot.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke() {
        repository.checkAuthState()
    }

}