package com.hercan.personneltrackingwithfacerecognition.ui.getstrangertrackingdata

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentGetStrangerTrackingDataBinding
import com.hercan.personneltrackingwithfacerecognition.ui.tracking.datelist.TrackingDateListAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class GetStrangerTrackingDataFragment : Fragment(R.layout.fragment_get_stranger_tracking_data) {

    private val binding by viewBinding(FragmentGetStrangerTrackingDataBinding::bind)
    private var list = ArrayList<String>()
    private lateinit var adapter: TrackingDateListAdapter
    private val args: GetStrangerTrackingDataFragmentArgs by navArgs()
    private lateinit var email: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = args.adminMail
        bindUI()
    }

    private fun bindUI() = with(binding) {
        list = ArrayList()
        rvDateList.layoutManager = LinearLayoutManager(activity)
        rvDateList.setHasFixedSize(true)
        adapter = TrackingDateListAdapter(ArrayList())
        rvDateList.adapter = adapter
        getData()
    }

    private fun getData() {
        val db = FirebaseStorage.getInstance()
        db.reference.child("$email/unknown").listAll().addOnSuccessListener { listResult ->
            if (listResult.prefixes.isEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.ivNotFolder.visibility = View.VISIBLE
                binding.rvDateList.visibility = View.GONE
            } else {
                for (item in listResult.prefixes) {
                    list.add(item.name)
                    binding.progressBar.visibility = View.GONE
                    binding.ivNotFolder.visibility = View.GONE
                    binding.rvDateList.visibility = View.VISIBLE
                }
                bindAdapter()
            }
        }.addOnFailureListener { _ ->
            binding.progressBar.visibility = View.GONE
            binding.ivNotFolder.visibility = View.VISIBLE
            binding.rvDateList.visibility = View.GONE
        }
    }

    private fun bindAdapter() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        list = list.sortedByDescending { dateFormat.parse(it) }.map { it }.toCollection(ArrayList())
        adapter = TrackingDateListAdapter(list)
        binding.rvDateList.adapter = adapter
        adapter.setItemClickListener {
            navigateToDetail(it)
        }
    }

    private fun navigateToDetail(str: String) {
        findNavController().navigate(
            GetStrangerTrackingDataFragmentDirections.navigateToStrangerTrackingListFragment(
                "$email/unknown/$str"
            )
        )
    }
}