package com.anggitdev.core.domain.repository

import com.anggitdev.core.data.Resource
import com.anggitdev.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserGithubRepository {

    fun searchUser(username: String): Flow<Resource<List<User>>>
    fun getDetailUser(username: String): Flow<Resource<User?>>
    fun getFollow(username: String, type: String): Flow<Resource<List<User>>>
    fun isFavorite(username: String): Flow<Boolean>
    suspend fun setFavorite(username: String, state: Boolean)
    fun getFavorites(): Flow<List<User>>
}