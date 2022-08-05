package com.anggitdev.myapplication.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anggitdev.core.data.Resource
import com.anggitdev.core.domain.model.User
import com.anggitdev.core.domain.usecase.UserGithubUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val userGithubUseCase: UserGithubUseCase): ViewModel() {
    private var _detailResponse: MutableLiveData<Resource<User?>> = MutableLiveData()
    val detailResponse: LiveData<Resource<User?>> = _detailResponse

    private var _isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getDetailUser(username: String) = viewModelScope.launch {
        userGithubUseCase.getDetailUser(username).collect{ values ->
            _detailResponse.postValue(values)
        }
    }

    fun setFavorite(username: String) = viewModelScope.launch {
        userGithubUseCase.setFavorite(username, true)
    }

    fun deleteFavorite(username: String) = viewModelScope.launch {
        userGithubUseCase.setFavorite(username, false)
    }

    fun isFavorite(username: String) = viewModelScope.launch {
        userGithubUseCase.isFavorite(username).collect{
            _isFavorite.postValue(it)
        }
    }
}