package com.hercan.personneltrackingwithfacerecognition.backend.ext

import android.util.Log
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
                Log.d(
                    "denemeleerr",
                    "Response Code: ${response.code()}, Response Message: ${response.message()}, Response Body: $errorBody"
                )
                return Resource.error(
                    Status.ERROR,
                    "Api call failed with code: ${response.code()}, message: ${response.message()}, error body: $errorBody"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace() // Hata ayıklama amacıyla hatayı logla
            //Resource.error(Status.ERROR, "Api call failed with exception: ${e.message}")
            Log.d("denemeleer", "${e.message}")
            return Resource.error(Status.ERROR, "Api call failed with exception: ${e.message}")
        }
    }
}