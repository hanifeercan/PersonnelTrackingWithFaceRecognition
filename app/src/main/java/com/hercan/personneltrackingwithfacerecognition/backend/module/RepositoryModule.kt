package com.hercan.personneltrackingwithfacerecognition.backend.module

import com.hercan.personneltrackingwithfacerecognition.backend.datasource.DataSourceImpl
import com.hercan.personneltrackingwithfacerecognition.backend.repository.Repository
import com.hercan.personneltrackingwithfacerecognition.backend.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        dataSource: DataSourceImpl
    ): Repository {
        return RepositoryImpl(dataSource)
    }
}
