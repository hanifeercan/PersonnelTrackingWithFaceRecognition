package com.hercan.personneltrackingwithfacerecognition.backend.datasource

import com.hercan.personneltrackingwithfacerecognition.backend.api.ApiService
import com.hercan.personneltrackingwithfacerecognition.backend.model.FaceRecognitionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import javax.inject.Inject

class DataSourceImpl @Inject constructor(private val api: ApiService) : DataSource {
    override suspend fun uploadImage(file: MultipartBody.Part): Call<FaceRecognitionResponse> {
        return api.uploadImage(file)
    }
}