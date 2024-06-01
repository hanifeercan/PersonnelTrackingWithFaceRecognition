package com.hercan.personneltrackingwithfacerecognition.ui.tracking.personneltracking

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class PersonnelTrackingItem(
    val name: String,
    val loginTimes: @RawValue ArrayList<TrackingTimes>,
    val outTimes: @RawValue ArrayList<TrackingTimes>
) : Parcelable

data class TrackingTimes(
    val id: String, val time: String
)

fun List<Timestamp>.toTrackingTimesList(): ArrayList<TrackingTimes> {
    val list = ArrayList<TrackingTimes>()
    this.forEachIndexed { index, timestamp ->

        val date: Date = timestamp.toDate()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        list.add(TrackingTimes((index + 1).toString(), sdf.format(date)))
    }
    return list
}

fun DocumentSnapshot.toPersonnelTrackingItem(): PersonnelTrackingItem {
    this.apply {
        val loginList = get("login_list") as List<Timestamp>
        val outList = get("out_list") as List<Timestamp>
        return PersonnelTrackingItem(
            id, loginList.toTrackingTimesList(), outList.toTrackingTimesList()
        )
    }
}

