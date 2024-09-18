package com.hercan.personneltrackingwithfacerecognition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        if (isLogged()) {
            navigateToHomeFragment()
        }
    }

    private fun isLogged(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    private fun navigateToHomeFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentContentMain) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.homeFragment)
    }
}