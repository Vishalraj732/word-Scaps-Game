package com.example.game.di

import com.example.game.data.repositoryImp.LevelRepositoryImp
import com.example.game.domain.repository.game.LevelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindLevelRepository(
        levelRepositoryImp: LevelRepositoryImp
    ): LevelRepository
}