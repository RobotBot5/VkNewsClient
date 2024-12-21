package com.robotbot.vknewsclient.domain

data class StatisticItem(
    val type: StatisticType,
    val count: Int = 0
)

enum class StatisticType {
    VIEW, SHARE, COMMENT, LIKE
}
