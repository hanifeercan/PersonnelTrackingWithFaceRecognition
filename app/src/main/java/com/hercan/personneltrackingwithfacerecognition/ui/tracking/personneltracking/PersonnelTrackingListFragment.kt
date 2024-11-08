package com.hercan.personneltrackingwithfacerecognition.ui.tracking.personneltracking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentPersonnelTrackingListBinding

class PersonnelTrackingListFragment : Fragment(R.layout.fragment_personnel_tracking_list) {
    private val binding by viewBinding(FragmentPersonnelTrackingListBinding::bind)
    private var list = ArrayList<PersonnelTrackingItem>()
    private lateinit var adapter: PersonnelTrackingListAdapter
    private lateinit var auth: FirebaseAuth
    private var date = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        bindUI()
    }

    private fun bindUI() = with(binding) {
        arguments.let {
            date = it?.let { it1 ->
                PersonnelTrackingListFragmentArgs.fromBundle(
                    it1
                ).date
            } ?: ""
            list = ArrayList()
            rvPersonnelList.layoutManager = LinearLayoutManager(activity)
            rvPersonnelList.setHasFixedSize(true)
            adapter = PersonnelTrackingListAdapter(ArrayList())
            rvPersonnelList.adapter = adapter
            getData()
        }
    }

    private fun getData() {
        if (date != "") {
            val currentUser = auth.currentUser
            val db = FirebaseFirestore.getInstance()
            db.collection(getString(R.string.company_tr)).document(currentUser?.email.toString())
                .collection(getString(R.string.tracking)).document(date).collection(date)
                .addSnapshotListener { value, _ ->
                    if (value != null) {
                        if (!value.isEmpty) {
                            val doc = value.documents
                            for (item in doc) {
                                binding.progressBar.visibility = View.GONE
                                binding.ivNotFolder.visibility = View.GONE
                                binding.rvPersonnelList.visibility = View.VISIBLE
                                list.add(item.toPersonnelTrackingItem())
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.ivNotFolder.visibility = View.VISIBLE
                            binding.rvPersonnelList.visibility = View.GONE
                        }
                        bindAdapter()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.ivNotFolder.visibility = View.VISIBLE
                        binding.rvPersonnelList.visibility = View.GONE
                    }
                }
        } else {
            binding.progressBar.visibility = View.GONE
            binding.ivNotFolder.visibility = View.VISIBLE
            binding.rvPersonnelList.visibility = View.GONE
        }
    }

    private fun bindAdapter() {
        adapter = PersonnelTrackingListAdapter(list)
        binding.rvPersonnelList.adapter = adapter
        adapter.setItemClickListener {
            navigateToDetail(it)
        }
    }

    private fun navigateToDetail(item: PersonnelTrackingItem) {
        findNavController().navigate(
            PersonnelTrackingListFragmentDirections.ToPersonnelTrackingDetailFragment(
                item
            )
        )
    }
}