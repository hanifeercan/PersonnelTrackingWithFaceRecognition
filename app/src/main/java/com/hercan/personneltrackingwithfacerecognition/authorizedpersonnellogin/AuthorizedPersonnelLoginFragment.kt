package com.hercan.personneltrackingwithfacerecognition.authorizedpersonnellogin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentAuthorizedPersonnelLoginBinding

class AuthorizedPersonnelLoginFragment : Fragment(R.layout.fragment_authorized_personnel_login) {
    private val
            binding by viewBinding(FragmentAuthorizedPersonnelLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAdminLogin.setOnClickListener {
            navigateToAdminLoginFragment()
        }
    }

    private fun navigateToAdminLoginFragment() {
        findNavController().navigate(AuthorizedPersonnelLoginFragmentDirections.navigateToAdministratorLoginFragment())
    }
}