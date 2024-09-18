package com.hercan.personneltrackingwithfacerecognition.backend.module

import com.hercan.personneltrackingwithfacerecognition.backend.api.ApiService
import com.hercan.personneltrackingwithfacerecognition.backend.datasource.DataSource
import com.hercan.personneltrackingwithfacerecognition.backend.datasource.DataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideDataSource(api: ApiService): DataSource {
        return DataSourceImpl(api)
    }
}