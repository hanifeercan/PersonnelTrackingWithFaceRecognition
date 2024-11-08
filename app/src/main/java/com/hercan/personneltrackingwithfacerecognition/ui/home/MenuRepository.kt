package com.hercan.personneltrackingwithfacerecognition.ui.home

import com.hercan.personneltrackingwithfacerecognition.R

class MenuRepository {
    private val menu = mutableListOf<MenuItem>()

    init {
        menu.add(MenuItem(1, R.drawable.ic_get_personnel_data, R.string.personnel_information_tr))
        menu.add(
            MenuItem(
                2, R.drawable.ic_get_personnel_tracking_data, R.string.entry_exit_tracking_tr
            )
        )
        menu.add(MenuItem(3, R.drawable.ic_add_personnel, R.string.new_personnel_registration_tr))
        menu.add(MenuItem(4, R.drawable.ic_new_authorized, R.string.create_authorization_account_tr))
        menu.add(MenuItem(5, R.drawable.ic_stranger, R.string.unknown_entry_tracking_tr))
        menu.add(MenuItem(6, R.drawable.ic_face_recognition, R.string.instant_face_recognition_tr))
    }

    fun getMenuList(): List<MenuItem> {
        return menu.toList()
    }
}