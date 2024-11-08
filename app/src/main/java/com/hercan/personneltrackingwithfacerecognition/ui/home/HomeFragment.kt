package com.hercan.personneltrackingwithfacerecognition.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hercan.personneltrackingwithfacerecognition.MainActivity
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var viewModel: HomeViewModel
    private lateinit var firestore: FirebaseFirestore
    private var authority: String = getString(R.string.security_tr)
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var adminMail: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        bindUI(authority)

        auth = Firebase.auth
        firestore = Firebase.firestore
        firestore.collectionGroup(getString(R.string.company_tr)).get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.id == auth.currentUser?.email) {
                    authority = document.get(getString(R.string.authority_tr)).toString()
                    bindUI(authority)
                    adminMail = if (authority == getString(R.string.security_tr)) {
                        document.get(getString(R.string.manager_tr)).toString()
                    } else {
                        auth.currentUser?.email
                    }
                }
            }
        }
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }


    private fun navigateToCreateAnAuthorizedAccountFragment() {
        findNavController().navigate(HomeFragmentDirections.navigateToCreateAnAuthorizedAccountFragment())
    }

    private fun navigateToAddNewPersonnelFragment() {
        findNavController().navigate(HomeFragmentDirections.navigateToAddNewPersonnelFragment())
    }

    private fun navigateToGetPersonnelDataFragment() {
        findNavController().navigate(HomeFragmentDirections.navigateToGetPersonnelDataFragment())
    }

    private fun navigateToFaceRecognitionFragment() {
        if (adminMail.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.try_again_after_your_authority_is_calculated_tr),
                Toast.LENGTH_LONG
            ).show()
        } else {
            findNavController().navigate(
                HomeFragmentDirections.navigateToInstantFaceRecognitionFragment(
                    adminMail!!
                )
            )
        }
    }

    private fun navigateToGetPersonnelTrackingDataFragment() {
        findNavController().navigate(HomeFragmentDirections.navigateToDateListFragment())
    }

    private fun navigateToGetStrangerTrackingDataFragment() {
        if (adminMail.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.try_again_after_your_authority_is_calculated_tr),
                Toast.LENGTH_LONG
            ).show()
        } else {
            findNavController().navigate(
                HomeFragmentDirections.navigateToGetStrangerTrackingDataFragment(
                    adminMail!!
                )
            )
        }
    }

    private fun bindUI(authority: String) = with(binding) {

        btnOut.setOnClickListener {
            auth.signOut()
            val navHostFragment =
                (requireActivity() as MainActivity).supportFragmentManager.findFragmentById(R.id.navHostFragmentContentMain) as NavHostFragment
            val navController = navHostFragment.navController
            findNavController().popBackStack(navController.graph.startDestinationId, false)
        }

        rvMenu.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            val menuAdapter = MenuAdapter(viewModel.getMenuList(), authority)
            this.adapter = menuAdapter
            menuAdapter.setItemClickListener {
                when (it.id) {
                    1 -> navigateToGetPersonnelDataFragment()
                    2 -> navigateToGetPersonnelTrackingDataFragment()
                    3 -> navigateToAddNewPersonnelFragment()
                    4 -> navigateToCreateAnAuthorizedAccountFragment()
                    5 -> navigateToGetStrangerTrackingDataFragment()
                    6 -> navigateToFaceRecognitionFragment()
                }
            }
        }
    }
}