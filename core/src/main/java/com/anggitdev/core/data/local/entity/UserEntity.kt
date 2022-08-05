package com.anggitdev.core.data.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "username")
    val login: String,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "followers")
    val followers: Int?,

    @ColumnInfo(name = "following")
    val following: Int?,

    @ColumnInfo(name = "company")
    val company: String? = null,

    @ColumnInfo(name = "location")
    val location: String? = null,

    @ColumnInfo(name = "public_repos")
    val publicRepos: Int? = null,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)