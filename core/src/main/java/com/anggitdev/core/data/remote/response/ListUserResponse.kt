package com.anggitdev.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListUserResponse(
    @SerializedName("total_count")
    val totalCount: Int?,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: List<UserResponse>
)
