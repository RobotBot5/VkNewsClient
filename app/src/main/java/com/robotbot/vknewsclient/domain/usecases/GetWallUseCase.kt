package com.robotbot.vknewsclient.domain.usecases

import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetWallUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    operator fun invoke(): StateFlow<List<FeedPost>> = repository.getWall()

}