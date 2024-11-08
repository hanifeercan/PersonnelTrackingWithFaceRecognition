package com.hercan.personneltrackingwithfacerecognition.ui.tracking.datelist

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
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentDateListBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DateListFragment : Fragment(R.layout.fragment_date_list) {

    private val binding by viewBinding(FragmentDateListBinding::bind)
    private var list = ArrayList<String>()
    private lateinit var adapter: TrackingDateListAdapter
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
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
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        db.collection("sirket").document(currentUser?.email.toString()).collection("tracking")
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    if (!value.isEmpty) {
                        val doc = value.documents
                        for (item in doc) {
                            binding.progressBar.visibility = View.GONE
                            binding.ivNotFolder.visibility = View.GONE
                            binding.rvDateList.visibility = View.VISIBLE
                            list.add(item.id)

                        }
                        if (list.isEmpty()) {
                            binding.progressBar.visibility = View.GONE
                            binding.ivNotFolder.visibility = View.VISIBLE
                            binding.rvDateList.visibility = View.GONE
                        }
                        bindAdapter()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.ivNotFolder.visibility = View.VISIBLE
                        binding.rvDateList.visibility = View.GONE
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.ivNotFolder.visibility = View.VISIBLE
                    binding.rvDateList.visibility = View.GONE
                }
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
            DateListFragmentDirections.navigateToPersonnelTrackingListFragment(
                str
            )
        )
    }
}