package com.anggitdev.core.domain.usecase

import com.anggitdev.core.data.Resource
import com.anggitdev.core.domain.model.User
import com.anggitdev.core.domain.repository.IUserGithubRepository
import kotlinx.coroutines.flow.Flow

class UserGithubInteractor (private val iUserGithubRepository: IUserGithubRepository) :
    UserGithubUseCase {
    override fun searchUser(username: String): Flow<Resource<List<User>>> =
        iUserGithubRepository.searchUser(username)

    override fun getDetailUser(username: String): Flow<Resource<User?>> =
        iUserGithubRepository.getDetailUser(username)

    override fun getFollow(username: String, type: String): Flow<Resource<List<User>>> =
        iUserGithubRepository.getFollow(username, type)

    override fun isFavorite(username: String): Flow<Boolean> =
        iUserGithubRepository.isFavorite(username)

    override suspend fun setFavorite(username: String, state: Boolean) =
        iUserGithubRepository.setFavorite(username, state)

    override fun getFavorites(): Flow<List<User>> =
        iUserGithubRepository.getFavorites()


}