package com.anggitdev.myapplication.di

import com.anggitdev.core.domain.usecase.UserGithubInteractor
import com.anggitdev.core.domain.usecase.UserGithubUseCase
import com.anggitdev.myapplication.ui.detail.DetailViewModel
import com.anggitdev.myapplication.ui.follow.FollowViewModel
import com.anggitdev.myapplication.ui.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserGithubUseCase> { UserGithubInteractor(get()) }
}

@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { FollowViewModel(get()) }

}