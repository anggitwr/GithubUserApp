package com.anggitdev.core.data.remote.network

import com.anggitdev.core.data.remote.response.ListUserResponse
import com.anggitdev.core.data.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun search(
        @Query("q") name: String
    ): ListUserResponse

    @GET("users/{username}")
    suspend fun getDetail(
        @Path("username") username: String
    ): UserResponse

    @GET("users/{username}/{type}")
    suspend fun getFollow(
        @Path("username") username: String,
        @Path("type") type: String
    ): List<UserResponse>
}