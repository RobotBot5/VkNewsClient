package com.robotbot.vknewsclient.ui.theme

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val feedPosts = viewModel.feedPosts.observeAsState(listOf())
    LazyColumn(
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = 8.dp,
            end = 8.dp,
            bottom = paddingValues.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(feedPosts.value, key = { it.id }) { post ->
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
                        viewModel.incrementStatisticItem(post, it)
                    },
                    onLikeClickListener = {
                        viewModel.incrementStatisticItem(post, it)
                    }
                )
            }
        }
    }
}