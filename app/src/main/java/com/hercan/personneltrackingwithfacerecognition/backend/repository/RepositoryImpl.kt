package com.hercan.personneltrackingwithfacerecognition.backend.repository

import com.hercan.personneltrackingwithfacerecognition.backend.datasource.DataSource
import com.hercan.personneltrackingwithfacerecognition.backend.ext.NetworkExt
import com.hercan.personneltrackingwithfacerecognition.backend.ext.Resource
import com.hercan.personneltrackingwithfacerecognition.backend.model.FaceRecognitionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataSource: DataSource,
) : NetworkExt(), Repository {
    override suspend fun uploadImage(file: MultipartBody.Part): Flow<Resource<FaceRecognitionResponse>> {
        return flow {
            emit(safeApiCall { dataSource.uploadImage(file) })
        }
    }
}