package com.hercan.personneltrackingwithfacerecognition.backend.api

import com.hercan.personneltrackingwithfacerecognition.backend.model.FaceRecognitionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/face_recognition")
    fun uploadImage(@Part file: MultipartBody.Part): Call<FaceRecognitionResponse>
}
