package com.hercan.personneltrackingwithfacerecognition.ui.getstrangertrackingdata

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentStrangerTrackingListBinding
import java.util.Date

class StrangerTrackingListFragment : Fragment(R.layout.fragment_stranger_tracking_list) {

    private val binding by viewBinding(FragmentStrangerTrackingListBinding::bind)
    private var list = ArrayList<StrangerTrackingModel>()
    private lateinit var adapter: StrangerTrackingListAdapter
    private val args: StrangerTrackingListFragmentArgs by navArgs()
    private lateinit var datePath: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePath = args.datePath
        bindUI()
    }

    private fun bindUI() = with(binding) {
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.setHasFixedSize(true)
        adapter = StrangerTrackingListAdapter()
        recyclerView.adapter = adapter
        getData()
    }

    private fun getData() {
        val db = FirebaseStorage.getInstance()
        db.reference.child(datePath).listAll().addOnSuccessListener { listResult ->
            if (listResult.items.isEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.ivNotFolder.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                for (item in listResult.items) {
                    item.downloadUrl.addOnSuccessListener { uri ->
                        item.metadata.addOnSuccessListener { metadata ->
                            val creationTimeMillis = metadata.creationTimeMillis
                            val time = Date(creationTimeMillis).toString()
                            list.add(StrangerTrackingModel(time, uri.toString()))
                            adapter.submitList(list)
                        }
                    }
                }
                binding.progressBar.visibility = View.GONE
                binding.ivNotFolder.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }.addOnFailureListener { _ ->
            binding.progressBar.visibility = View.GONE
            binding.ivNotFolder.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
    }
}