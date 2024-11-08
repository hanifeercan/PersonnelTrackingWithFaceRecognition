package com.hercan.personneltrackingwithfacerecognition.ui.administratorlogin

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
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        bindUI()
    }

    private fun bindUI() = with(binding) {
        tvSignup.setOnClickListener { navigateToAdminSingUpFragment() }
        btnLogin.setOnClickListener { login() }
    }

    private fun navigateToAdminSingUpFragment() {
        findNavController().navigate(LoginFragmentDirections.navigateToAdministratorSingUpFragment())
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(LoginFragmentDirections.navigateToHomeFragment())
    }

    private fun login() {
        val email = binding.etMail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email == "") {
            Toast.makeText(activity, R.string.enter_email_tr, Toast.LENGTH_LONG).show()
        } else if (password == "") {
            Toast.makeText(activity, R.string.enter_password_tr, Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                navigateToHomeFragment()
            }.addOnFailureListener {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}