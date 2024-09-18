package com.hercan.personneltrackingwithfacerecognition.backend.datasource

import com.hercan.personneltrackingwithfacerecognition.backend.model.FaceRecognitionResponse
import okhttp3.MultipartBody
import retrofit2.Call

interface DataSource {
    suspend fun uploadImage(file: MultipartBody.Part): Call<FaceRecognitionResponse>
}
