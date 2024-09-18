package com.hercan.personneltrackingwithfacerecognition.ui.getpersonneldata

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentGetPersonnelDataDetailBinding
import com.squareup.picasso.Picasso

class GetPersonnelDataDetailFragment : Fragment(R.layout.fragment_get_personnel_data_detail) {

    private val binding by viewBinding(FragmentGetPersonnelDataDetailBinding::bind)
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        bindUI()
    }

    private fun bindUI() = with(binding) {
        arguments.let {
            val personnel =
                it?.let { it1 -> GetPersonnelDataDetailFragmentArgs.fromBundle(it1).personnel }
            tvNameAndSurname.text = personnel?.name
            tvTc.text = personnel?.tc
            tvBirthday.text = personnel?.birthday
            tvDepartment.text = personnel?.department
            tvRegistrationDate.text = personnel?.registrationDate

            if (personnel?.photo != "") {
                ivPersonnelPhoto.setBackgroundResource(0)
                Picasso.get().load(personnel?.photo).into(ivPersonnelPhoto)
            }
        }

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(
                ContextCompat.getColor(requireContext(), android.R.color.white),
                ContextCompat.getColor(requireContext(), R.color.green_hornet)
            )
        )
        layoutBackground.background = gradientDrawable
    }
}
