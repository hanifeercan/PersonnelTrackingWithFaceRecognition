package com.hercan.personneltrackingwithfacerecognition.tracking.personneltrackingdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentPersonnelTrackingDetailBinding

class PersonnelTrackingDetailFragment : Fragment(R.layout.fragment_personnel_tracking_detail) {
    private val binding by viewBinding(FragmentPersonnelTrackingDetailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = with(binding) {
        arguments.let {
            val personnel = it?.let { it1 ->
                PersonnelTrackingDetailFragmentArgs.fromBundle(
                    it1
                ).personnel
            }
            if (personnel != null) {
                tvNameAndSurname.text = personnel.name
                rvTrackingList.layoutManager = LinearLayoutManager(activity)
                rvTrackingList.setHasFixedSize(true)
                rvTrackingList.adapter =
                    PersonnelTrackingDetailListAdapter(personnel.loginTimes, personnel.outTimes)
            }
        }
    }
}