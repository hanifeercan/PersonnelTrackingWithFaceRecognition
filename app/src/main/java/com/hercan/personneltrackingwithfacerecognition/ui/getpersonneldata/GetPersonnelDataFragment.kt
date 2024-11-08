package com.hercan.personneltrackingwithfacerecognition.ui.getpersonneldata

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
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentGetPersonnelDataBinding

class GetPersonnelDataFragment : Fragment(R.layout.fragment_get_personnel_data) {

    private val binding by viewBinding(FragmentGetPersonnelDataBinding::bind)
    private var list = ArrayList<Personnel>()
    private lateinit var adapter: PersonnelListAdapter
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        bindUI()
    }

    private fun bindUI() = with(binding) {
        list = ArrayList()
        rvPersonnelList.layoutManager = LinearLayoutManager(activity)
        rvPersonnelList.setHasFixedSize(true)
        adapter = PersonnelListAdapter(ArrayList())
        rvPersonnelList.adapter = adapter
        getData()
    }

    private fun getData() {
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        db.collection(getString(R.string.company_tr)).document(currentUser?.email.toString()).collection(getString(R.string.personnel))
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    if (!value.isEmpty) {
                        val doc = value.documents
                        for (item in doc) {
                            binding.progressBar.visibility = View.GONE
                            binding.ivNotFolder.visibility = View.GONE
                            binding.rvPersonnelList.visibility = View.VISIBLE
                            list.add(item.toPersonnel())

                        }
                        if (list.isEmpty()) {
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
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.ivNotFolder.visibility = View.VISIBLE
                    binding.rvPersonnelList.visibility = View.GONE
                }
            }
    }

    private fun bindAdapter() {
        adapter = PersonnelListAdapter(list)
        binding.rvPersonnelList.adapter = adapter
        adapter.setItemClickListener {
            navigateToDetail(it)
        }
    }

    private fun navigateToDetail(item: Personnel) {
        findNavController().navigate(
            GetPersonnelDataFragmentDirections.navigateToGetPersonnelDataDetailFragment(item)
        )
    }
}