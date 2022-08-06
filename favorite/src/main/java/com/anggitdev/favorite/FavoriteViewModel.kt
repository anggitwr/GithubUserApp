package com.anggitdev.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anggitdev.core.domain.model.User
import com.anggitdev.core.domain.usecase.UserGithubUseCase
import kotlinx.coroutines.launch

class FavoriteViewModel(private val userGithubUseCase: UserGithubUseCase) : ViewModel() {
    private var _favoritesResponse: MutableLiveData<List<User>> = MutableLiveData()
    val favoritesResponse get() = _favoritesResponse
    fun getFavorites() = viewModelScope.launch {
        userGithubUseCase.getFavorites().collect {
            _favoritesResponse.postValue(it)
        }
    }
}