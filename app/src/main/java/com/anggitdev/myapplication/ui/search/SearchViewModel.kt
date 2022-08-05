package com.anggitdev.myapplication.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anggitdev.core.data.Resource
import com.anggitdev.core.domain.model.User
import com.anggitdev.core.domain.usecase.UserGithubUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch

@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(private val userGithubUseCase: UserGithubUseCase) : ViewModel() {

    private var _searchResponse: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    val searchResponse get() = _searchResponse
    fun searchUser(name: String) = viewModelScope.launch {
        userGithubUseCase.searchUser(name).collect {
            searchResponse.postValue(it)
        }
    }
}