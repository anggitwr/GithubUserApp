package com.anggitdev.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val username: String,
    val name: String?,
    val avatarUrl: String,
    val followers: Int?,
    val following: Int?,
    val company: String?,
    val location: String?,
    val publicRepos: Int? = null,
    var isFavorite: Boolean = false,
): Parcelable
