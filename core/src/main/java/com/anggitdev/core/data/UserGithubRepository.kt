package com.anggitdev.core.data

import com.anggitdev.core.data.local.LocalDataSource
import com.anggitdev.core.data.remote.RemoteDataSource
import com.anggitdev.core.data.remote.network.ApiResponse
import com.anggitdev.core.data.remote.response.UserResponse
import com.anggitdev.core.domain.model.User
import com.anggitdev.core.domain.repository.IUserGithubRepository
import com.anggitdev.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class UserGithubRepository(private val remoteDataSource: RemoteDataSource,
                           private val localDataSource: LocalDataSource
): IUserGithubRepository {

    override fun searchUser(username: String): Flow<Resource<List<User>>> =
        object : NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun loadFromDB(): Flow<List<User>> {
                return localDataSource.search(username).map { userEntity ->
                    DataMapper.mapEntitiesToDomain(userEntity)
                }
            }

            override fun shouldFetch(data: List<User>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.search(username)

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val userList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertUser(userList)
            }
        }.asFlow()

    override fun getDetailUser(username: String): Flow<Resource<User?>> =
        object : NetworkBoundResource<User?, UserResponse>() {
            override fun loadFromDB(): Flow<User?> {
                return localDataSource.getUser(username).map {
                    it?.let {
                        DataMapper.mapEntityToDomain(it)
                    }
                }
            }

            override fun shouldFetch(data: User?): Boolean = data?.isFavorite == false

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> =
                remoteDataSource.getDetail(username)

            override suspend fun saveCallResult(data: UserResponse) {
                val user = DataMapper.mapResponsesToEntity(data)
                localDataSource.insertUser(user)
            }
        }.asFlow()

    override fun getFollow(username: String, type: String): Flow<Resource<List<User>>> = flow {
        val response = remoteDataSource.getFollow(username, type)
        when (val apiResponse = response.first()) {
            is ApiResponse.Success -> {
                val userList = DataMapper.mapResponsesToEntities(apiResponse.data)
                localDataSource.insertUser(userList)
                emit(Resource.Success(DataMapper.mapEntitiesToDomain(userList)))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Empty())
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(apiResponse.errorMessage))
            }
        }
    }

    override fun isFavorite(username: String): Flow<Boolean> {
        return localDataSource.isFavorite(username)
    }

    override suspend fun setFavorite(username: String, state: Boolean) {
        localDataSource.updateFavorite(username, state)
    }

    override fun getFavorites(): Flow<List<User>> = flow {
        val favorites = DataMapper.mapEntitiesToDomain(localDataSource.getFavorites())
        emit(favorites)
    }.flowOn(Dispatchers.IO)
}