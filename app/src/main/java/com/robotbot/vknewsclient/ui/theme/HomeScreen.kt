package com.robotbot.vknewsclient.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.robotbot.vknewsclient.MainViewModel
import com.robotbot.vknewsclient.domain.FeedPost

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val screenState = viewModel.state.observeAsState(HomeScreenState.Initial)

    when(val state = screenState.value) {
        is HomeScreenState.Posts -> {
            FeedPosts(
                viewModel = viewModel,
                paddingValues = paddingValues,
                posts = state.posts
            )
        }

        is HomeScreenState.Comments -> {
            CommentsScreen(
                feedPost = state.feedPost,
                comments = state.comments,
                paddingValuesFromMainScreen = paddingValues,
                onBackPressed = {
                    viewModel.closeComments()
                }
            )
            BackHandler {
                viewModel.closeComments()
            }
        }
        HomeScreenState.Initial -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    viewModel: MainViewModel,
    paddingValues: PaddingValues,
    posts: List<FeedPost>
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
                    onViewsClickListener = {
                        viewModel.incrementStatisticItem(post, it)
                    },
                    onShareClickListener = {
                        viewModel.incrementStatisticItem(post, it)
                    },
                    onCommentClickListener = {
                        viewModel.showComments(post)
                    },
                    onLikeClickListener = {
                        viewModel.incrementStatisticItem(post, it)
                    }
                )
            }
        }
    }
}