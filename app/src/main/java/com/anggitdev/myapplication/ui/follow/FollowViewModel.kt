package com.anggitdev.myapplication.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anggitdev.core.data.Resource
import com.anggitdev.core.domain.model.User
import com.anggitdev.core.domain.usecase.UserGithubUseCase
import kotlinx.coroutines.launch

class FollowViewModel(private val userGithubUseCase: UserGithubUseCase): ViewModel(){

    private var _followRespon: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    val followResponse: LiveData<Resource<List<User>>> = _followRespon

    fun getFollow(username: String, type: FollowFragment.FollowType) = viewModelScope.launch {
        userGithubUseCase.getFollow(
            username, when(type){
            FollowFragment.FollowType.FOLLOWERS -> "followers"
            FollowFragment.FollowType.FOLLOWING -> "following"
        }).collect{
            _followRespon.postValue(it)
        }
    }

}