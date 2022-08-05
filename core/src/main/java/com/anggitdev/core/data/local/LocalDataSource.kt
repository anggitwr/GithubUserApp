package com.anggitdev.core.data.local

import com.anggitdev.core.data.local.entity.UserEntity
import com.anggitdev.core.data.local.room.UserGithubDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource (private val userGithubDao: UserGithubDao) {
    fun search(name: String): Flow<List<UserEntity>> = userGithubDao.search(name)
    suspend fun insertUser(userList: List<UserEntity>) = userGithubDao.insertUser(userList)
    suspend fun insertUser(userList: UserEntity) = userGithubDao.insertUser(userList)
    fun getUser(username: String): Flow<UserEntity?> = userGithubDao.getUser(username)

    fun isFavorite(username: String): Flow<Boolean> = userGithubDao.isFavorite(username)
    suspend fun updateFavorite(username: String, state: Boolean) =
        userGithubDao.updateFavorite(username, state)

    suspend fun getFavorites(): List<UserEntity> = userGithubDao.getFavorites()
}