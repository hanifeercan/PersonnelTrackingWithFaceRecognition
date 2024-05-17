package com.hercan.personneltrackingwithfacerecognition.getpersonneldata

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Personnel(
    val tc: String,
    val name: String,
    val birthday: String,
    val department: String,
    val registrationDate: String,
    val photo: String
) : Parcelable

fun DocumentSnapshot.toPersonnel(): Personnel {
    this.apply {
        val name = get("name").toString() + " " + get("surname")
        val tc = get("tc").toString()
        val department = get("department").toString().ifEmpty { "-" }
        val registrationDate = get("registrationDate").toString()
        val birthday = get("birthday").toString().ifEmpty { "-" }
        val downloadUrl = get("downloadUrl").toString()
        return Personnel(
            tc, name, birthday, department, registrationDate, downloadUrl
        )
    }
}