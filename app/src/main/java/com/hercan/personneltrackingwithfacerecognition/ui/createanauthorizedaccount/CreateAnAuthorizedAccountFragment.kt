package com.hercan.personneltrackingwithfacerecognition.ui.createanauthorizedaccount

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentCreateAnAuthorizedAccountBinding

class CreateAnAuthorizedAccountFragment : Fragment(R.layout.fragment_create_an_authorized_account) {
    private val binding by viewBinding(FragmentCreateAnAuthorizedAccountBinding::bind)
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore

        binding.btnCreate.setOnClickListener { create() }
    }

    private fun create() {
        val email = binding.etMail.text.toString()
        val password = binding.etPassword.text.toString()
        val currentUser = auth.currentUser

        if (email == "") {
            Toast.makeText(activity, R.string.enter_email_tr, Toast.LENGTH_LONG).show()
        } else if (password == "") {
            Toast.makeText(activity, R.string.enter_password_tr, Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                val postMap = hashMapOf<String, Any>()
                postMap[getString(R.string.email)] = email
                postMap[getString(R.string.password_tr)] = password
                postMap[getString(R.string.authority_tr)] = getString(R.string.security_tr)
                postMap[getString(R.string.manager_tr)] = currentUser?.email.toString()

                firestore.collection(getString(R.string.company_tr)).document(email).set(postMap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (auth.currentUser != null) {
                            auth.signOut()
                            Toast.makeText(activity, R.string.authority_created_tr, Toast.LENGTH_LONG)
                                .show()
                            findNavController().navigate(CreateAnAuthorizedAccountFragmentDirections.navigateToAdministratorLoginFragment())
                        }
                    } else {
                        Toast.makeText(activity, R.string.error_occurred_tr, Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
