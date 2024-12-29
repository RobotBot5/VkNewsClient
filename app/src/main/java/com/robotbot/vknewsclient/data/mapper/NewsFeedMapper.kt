package com.robotbot.vknewsclient.data.mapper

import android.util.Log
import com.robotbot.vknewsclient.data.model.NewsFeedResponseDto
import com.robotbot.vknewsclient.domain.FeedPost
import com.robotbot.vknewsclient.domain.StatisticItem
import com.robotbot.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapResponseToPost(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val groups = responseDto.newsFeedContent.groups
        val posts = responseDto.newsFeedContent.posts

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue
            Log.d("NewsFeedMapper", post.toString())
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityTitle = group.name,
                publicationDate = mapTimestampToDate(post.date * 1000),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKE, post.likes.count),
                    StatisticItem(type = StatisticType.VIEW, post.views.count),
                    StatisticItem(type = StatisticType.SHARE, post.reposts.count),
                    StatisticItem(type = StatisticType.COMMENT, post.comments.count)
                ),
                isLiked = post.likes.userLikes > 0
            )
            result.add(feedPost)
        }

        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

}