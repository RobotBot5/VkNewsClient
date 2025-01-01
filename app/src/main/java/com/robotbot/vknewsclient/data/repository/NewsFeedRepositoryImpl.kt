package com.robotbot.vknewsclient.data.repository

import android.app.Application
import com.robotbot.vknewsclient.data.mapper.NewsFeedMapper
import com.robotbot.vknewsclient.data.network.ApiFactory
import com.robotbot.vknewsclient.domain.entity.AuthState
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.entity.PostComment
import com.robotbot.vknewsclient.domain.entity.StatisticItem
import com.robotbot.vknewsclient.domain.entity.StatisticType
import com.robotbot.vknewsclient.domain.repository.NewsFeedRepository
import com.robotbot.vknewsclient.extentions.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepositoryImpl(application: Application) : NewsFeedRepository {

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token
        get() = VKAccessToken.restore(storage)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadWall(getAccessToken())
            } else {
                apiService.loadWall(getAccessToken(), startFrom)
            }
            val posts = mapper.mapResponseToPost(response)
            _feedPosts.addAll(posts)
            nextFrom = response.newsFeedContent.nextFrom
            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.catch {

    }

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)

    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val currentToken = token
            val loggedIn = currentToken != null && currentToken.isValid
            val authState = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
            emit(authState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    override suspend fun checkAuthState() {
        checkAuthStateEvents.emit(Unit)
    }

    private val wall: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    override fun getAuthStateFlow(): StateFlow<AuthState> = authStateFlow

    override fun getWall(): StateFlow<List<FeedPost>> = wall

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override fun getComments(post: FeedPost): StateFlow<List<PostComment>> = flow {
        val comments = apiService.getComments(
            token = getAccessToken(),
            ownerId = post.communityId,
            postId = post.id
        )
        emit(mapper.mapResponseToComments(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override suspend fun deletePost(post: FeedPost) {
        apiService.deletePost(
            token = getAccessToken(),
            ownerId = post.communityId,
            postId = post.id
        )
        _feedPosts.remove(post)
        refreshedListFlow.emit(feedPosts)
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKE }
            add(StatisticItem(type = StatisticType.LIKE, count = newLikesCount))
        }
        val newPost = feedPost.copy(
            statistics = newStatistics,
            isLiked = !feedPost.isLiked
        )
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}