package com.hercan.personneltrackingwithfacerecognition.backend.repository

import com.hercan.personneltrackingwithfacerecognition.backend.ext.Resource
import com.hercan.personneltrackingwithfacerecognition.backend.model.FaceRecognitionResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface Repository {

    suspend fun uploadImage(file: MultipartBody.Part): Flow<Resource<FaceRecognitionResponse>>
}