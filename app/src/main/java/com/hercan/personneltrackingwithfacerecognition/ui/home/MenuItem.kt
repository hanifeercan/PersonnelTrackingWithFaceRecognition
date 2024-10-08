package com.hercan.personneltrackingwithfacerecognition.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MenuItem(
    val id: Int, @DrawableRes val image: Int, @StringRes val text: Int
)