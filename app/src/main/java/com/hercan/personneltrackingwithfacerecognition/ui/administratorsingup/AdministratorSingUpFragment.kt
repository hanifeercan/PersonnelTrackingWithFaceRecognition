package com.hercan.personneltrackingwithfacerecognition.ui.administratorsingup

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
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentAdministratorSingUpBinding

class AdministratorSingUpFragment : Fragment(R.layout.fragment_administrator_sing_up) {

    private val binding by viewBinding(FragmentAdministratorSingUpBinding::bind)
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore
        binding.btnSingUp.setOnClickListener { signup() }
    }

    private fun login() {
        Toast.makeText(activity, R.string.can_login_tr, Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

    private fun signup() {
        val email = binding.etMail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email == "") {
            Toast.makeText(activity, R.string.enter_email_tr, Toast.LENGTH_LONG).show()
        } else if (password == "") {
            Toast.makeText(activity, R.string.enter_password_tr, Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                val postMap = hashMapOf<String, Any>()
                postMap[getString(R.string.email)] = email
                postMap[getString(R.string.password_tr)] = password
                postMap[getString(R.string.authority_tr)] = getString(R.string.manager_tr)

                firestore.collection(getString(R.string.company_tr)).document(email).set(postMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            login()
                        } else {
                            Toast.makeText(activity, R.string.error_occurred_tr, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }
    }
}