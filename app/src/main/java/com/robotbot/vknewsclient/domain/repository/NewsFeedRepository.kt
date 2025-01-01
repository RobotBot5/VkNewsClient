package com.robotbot.vknewsclient.domain.repository

import com.robotbot.vknewsclient.domain.entity.AuthState
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.entity.PostComment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getWall(): StateFlow<List<FeedPost>>

    fun getComments(post: FeedPost): StateFlow<List<PostComment>>

    suspend fun loadNextData()

    suspend fun checkAuthState()

    suspend fun deletePost(post: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)
}