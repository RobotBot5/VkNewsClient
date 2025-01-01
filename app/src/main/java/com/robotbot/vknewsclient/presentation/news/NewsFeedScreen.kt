package com.robotbot.vknewsclient.presentation.news

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.presentation.ViewModelFactory
import com.robotbot.vknewsclient.presentation.getApplicationComponent
import com.robotbot.vknewsclient.ui.theme.DarkBlue

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    Log.d("RECOMPOSITION_TAG", "NewsFeedScreen")
    val component = getApplicationComponent()
    val viewModel: NewsFeedViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.state.collectAsState(NewsFeedScreenState.Initial)

    NewsFeedScreenContent(
        screenState = screenState,
        paddingValues = paddingValues,
        onCommentClickListener = onCommentClickListener,
        viewModel = viewModel
    )
}

@Composable
private fun NewsFeedScreenContent(
    screenState: State<NewsFeedScreenState>,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    viewModel: NewsFeedViewModel
) {
    when(val state = screenState.value) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                viewModel = viewModel,
                paddingValues = paddingValues,
                posts = state.posts,
                nextDataIsLoading = state.nextDataIsLoading,
                onCommentClickListener = onCommentClickListener
            )
        }
        NewsFeedScreenState.Initial -> {

        }

        NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    viewModel: NewsFeedViewModel,
    paddingValues: PaddingValues,
    posts: List<FeedPost>,
    nextDataIsLoading: Boolean,
    onCommentClickListener: (FeedPost) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = 8.dp,
            end = 8.dp,
            bottom = paddingValues.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            val dismissState = rememberSwipeToDismissBoxState()

            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                viewModel.deletePost(post)
            }

            SwipeToDismissBox(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = {}
            ) {
                PostCard(
                    feedPost = post,
                    onCommentClickListener = {
                        onCommentClickListener(post)
                    },
                    onLikeClickListener = {
                        viewModel.changeLikeStatus(feedPost = post)
                    }
                )
            }
        }
        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                SideEffect {
                    viewModel.loadNextWall()
                }
            }
        }
    }
}