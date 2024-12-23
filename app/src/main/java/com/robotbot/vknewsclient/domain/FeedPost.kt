package com.robotbot.vknewsclient.domain

import com.robotbot.vknewsclient.R

data class FeedPost(
    val id: Int,
    val communityTitle: String = "/dev/null",
    val communityAvatarResId: Int = R.drawable.post_comunity_thumbnail,
    val publicationDate: String = "14:00",
    val contentText: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
    val contentImageResId: Int = R.drawable.post_content_image,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = StatisticType.VIEW, count = 535),
        StatisticItem(type = StatisticType.SHARE, count = 152),
        StatisticItem(type = StatisticType.COMMENT, count = 525),
        StatisticItem(type = StatisticType.LIKE, count = 757),
    )
)
