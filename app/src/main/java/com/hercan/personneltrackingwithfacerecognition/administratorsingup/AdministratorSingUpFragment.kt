package com.hercan.personneltrackingwithfacerecognition.administratorsingup

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
        Toast.makeText(activity, R.string.giris_yapabilirsiniz, Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

    private fun signup() {
        val email = binding.etMail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email == "") {
            Toast.makeText(activity, R.string.e_mail_giriniz, Toast.LENGTH_LONG).show()
        } else if (password == "") {
            Toast.makeText(activity, R.string.sifre_giriniz, Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                val postMap = hashMapOf<String, Any>()
                postMap["email"] = email
                postMap["sifre"] = password
                postMap["yetki"] = "yonetici"

                firestore.collection("sirket").document(email).set(postMap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        login()
                    } else {
                        Toast.makeText(activity, R.string.hata_olustu, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}