package com.anggitdev.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anggitdev.core.data.local.entity.UserEntity
import com.anggitdev.core.data.local.room.UserGithubDao


@Database(
    entities =[UserEntity::class],
    version =1,
    exportSchema =false
)
abstract class UserGithubDatabase : RoomDatabase(){
    abstract fun userGithubDao(): UserGithubDao
}