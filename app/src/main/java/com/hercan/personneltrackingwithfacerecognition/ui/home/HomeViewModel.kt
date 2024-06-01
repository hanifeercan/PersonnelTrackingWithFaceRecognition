package com.hercan.personneltrackingwithfacerecognition.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    fun getMenuList(): List<MenuItem> {
        val repository = MenuRepository()
        return repository.getMenuList()
    }
}