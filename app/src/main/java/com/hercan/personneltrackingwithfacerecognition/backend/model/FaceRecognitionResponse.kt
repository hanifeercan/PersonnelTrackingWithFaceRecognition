package com.hercan.personneltrackingwithfacerecognition.backend.model

data class FaceRecognitionResponse(
    val peopleNames: List<String>?, val errorMessage: String?
)