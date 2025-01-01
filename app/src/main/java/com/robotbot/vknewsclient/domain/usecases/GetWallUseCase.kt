package com.robotbot.vknewsclient.domain.usecases

import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetWallUseCase(
    private val repository: NewsFeedRepository
) {

    operator fun invoke(): StateFlow<List<FeedPost>> = repository.getWall()

}