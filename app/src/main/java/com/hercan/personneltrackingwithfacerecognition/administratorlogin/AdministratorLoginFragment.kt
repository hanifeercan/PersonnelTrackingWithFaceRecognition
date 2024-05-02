package com.hercan.personneltrackingwithfacerecognition.administratorlogin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentAdministratorLoginBinding

class AdministratorLoginFragment : Fragment(R.layout.fragment_administrator_login) {
    private val
            binding by viewBinding(FragmentAdministratorLoginBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSignup.setOnClickListener {
            navigateToAdminSingUpFragment()
        }
        binding.btnLogin.setOnClickListener { login() }
        auth = Firebase.auth
    }

    private fun navigateToAdminSingUpFragment() {
        findNavController().navigate(AdministratorLoginFragmentDirections.navigateToAdministratorSingUpFragment())
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(AdministratorLoginFragmentDirections.navigateToHomeFragment())
    }

    private fun login() {
        val email = binding.etMail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email == "") {
            Toast.makeText(activity, R.string.e_mail_giriniz, Toast.LENGTH_LONG).show()
        } else if (password == "") {
            Toast.makeText(activity, R.string.sifre_giriniz, Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                navigateToHomeFragment()
            }.addOnFailureListener {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}