package com.anggitdev.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("followers")
    val followers: Int?,

    @SerializedName("following")
    val following: Int?,

    @SerializedName("company")
    val company: String? = null,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("public_repos")
    val publicRepos: Int? = null
)