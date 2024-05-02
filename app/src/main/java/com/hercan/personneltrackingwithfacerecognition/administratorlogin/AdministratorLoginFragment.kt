package com.hercan.personneltrackingwithfacerecognition.administratorlogin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentAdministratorLoginBinding

class AdministratorLoginFragment : Fragment(R.layout.fragment_administrator_login) {
    private val
            binding by viewBinding(FragmentAdministratorLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSignup.setOnClickListener {
            navigateToAdminSingUpFragment()
        }
    }

    private fun navigateToAdminSingUpFragment() {
        findNavController().navigate(AdministratorLoginFragmentDirections.navigateToAdministratorSingUpFragment())
    }
}