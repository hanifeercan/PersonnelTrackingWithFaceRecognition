package com.hercan.personneltrackingwithfacerecognition.home

import com.hercan.personneltrackingwithfacerecognition.R

class MenuRepository {
    private val menu = mutableListOf<MenuItem>()

    init {
        menu.add(MenuItem(1, R.drawable.ic_get_personnel_data, R.string.personel_bilgileri))
        menu.add(
            MenuItem(
                2,
                R.drawable.ic_get_personnel_tracking_data,
                R.string.giris_cikis_takibi
            )
        )
        menu.add(MenuItem(3, R.drawable.ic_add_personnel, R.string.yeni_personel_kayit))
        menu.add(MenuItem(4, R.drawable.ic_new_authorized, R.string.yetkilendirme_hesabi_olustur))
        menu.add(MenuItem(5, R.drawable.ic_face_recognition, R.string.anlik_yuz_tanima))
        menu.add(MenuItem(6, R.drawable.ic_stranger, R.string.bilinmeyen_giris_takip))
    }

    fun getMenuList(): List<MenuItem> {
        return menu.toList()
    }
}