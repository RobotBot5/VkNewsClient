package com.robotbot.vknewsclient.domain.usecases

import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke(feedPost: FeedPost) {
        repository.changeLikeStatus(feedPost)
    }

}