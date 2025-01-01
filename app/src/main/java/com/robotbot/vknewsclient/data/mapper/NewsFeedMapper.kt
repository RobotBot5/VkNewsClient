package com.robotbot.vknewsclient.data.mapper

import com.robotbot.vknewsclient.data.model.CommentsResponseDto
import com.robotbot.vknewsclient.data.model.NewsFeedResponseDto
import com.robotbot.vknewsclient.domain.entity.FeedPost
import com.robotbot.vknewsclient.domain.entity.PostComment
import com.robotbot.vknewsclient.domain.entity.StatisticItem
import com.robotbot.vknewsclient.domain.entity.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsFeedMapper @Inject constructor() {

    fun mapResponseToPost(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val groups = responseDto.newsFeedContent.groups
        val posts = responseDto.newsFeedContent.posts

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue
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

    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val authors = response.commentsContent.authors
        val comments = response.commentsContent.comments

        for (comment in comments) {
            if (comment.text.isBlank()) continue
            val author = authors.find { it.id == comment.authorId } ?: continue

            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date * 1000)
            )
            result.add(postComment)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

}