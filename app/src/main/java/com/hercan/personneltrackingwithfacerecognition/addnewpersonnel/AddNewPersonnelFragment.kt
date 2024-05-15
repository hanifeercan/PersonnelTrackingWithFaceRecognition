package com.hercan.personneltrackingwithfacerecognition.addnewpersonnel

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentAddNewPersonnelBinding

class AddNewPersonnelFragment : Fragment(R.layout.fragment_add_new_personnel) {

    private val binding by viewBinding(FragmentAddNewPersonnelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI() = with(binding) {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(
                ContextCompat.getColor(requireContext(), android.R.color.white),
                ContextCompat.getColor(requireContext(), R.color.green_hornet)
            )
        )
        binding.layoutBackground.background = gradientDrawable
    }
}
