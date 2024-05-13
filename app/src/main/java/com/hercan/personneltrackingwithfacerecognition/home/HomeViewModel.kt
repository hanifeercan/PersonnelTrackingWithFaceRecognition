package com.hercan.personneltrackingwithfacerecognition.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    fun getMenuList(): List<MenuItem> {
        val repository = MenuRepository()
        return repository.getMenuList()
    }
}