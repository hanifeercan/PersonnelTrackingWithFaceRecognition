package com.hercan.personneltrackingwithfacerecognition.backend.ext

import retrofit2.Call

abstract class NetworkExt {
    suspend fun <T> safeApiCall(call: suspend () -> Call<T>): Resource<T> {
        try {
            val response = call.invoke().execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.success(body)
                } else {
                    return Resource.error(
                        Status.ERROR, "Opps! Something went wrong."
                    )
                }
            } else {
                val errorBody = response.errorBody()?.string()
                return Resource.error(
                    Status.ERROR,
                    "Api call failed with code: ${response.code()}, message: ${response.message()}, error body: $errorBody"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.error(Status.ERROR, "Servis bağlantısı sağlanamadı.")
        }
    }
}