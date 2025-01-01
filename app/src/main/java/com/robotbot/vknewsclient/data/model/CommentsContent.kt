package com.robotbot.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class CommentsContent(
    @SerializedName("items") val comments: List<CommentDto>,
    @SerializedName("profiles") val authors: List<CommentAuthorDto>
)
