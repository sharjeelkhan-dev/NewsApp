package com.sharjeel.newsapp.di

import com.sharjeel.newsapp.data.repository.AuthRepositoryImpl
import com.sharjeel.newsapp.data.repository.NewsRepositoryImpl
import com.sharjeel.newsapp.domain.repository.AuthRepository
import com.sharjeel.newsapp.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}
