package com.hercan.personneltrackingwithfacerecognition.tracking.personneltrackingdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hercan.personneltrackingwithfacerecognition.databinding.ItemTrackingDetailBinding
import com.hercan.personneltrackingwithfacerecognition.tracking.personneltracking.TrackingTimes

class PersonnelTrackingDetailListAdapter(
    private val login: ArrayList<TrackingTimes>,
    private val out: ArrayList<TrackingTimes>
) : RecyclerView.Adapter<PersonnelTrackingDetailListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemTrackingDetailBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(login: TrackingTimes, position: Int) = with(binding) {

            tvId.text = login.id
            tvLoginDateTime.text = login.time.split(" ")[1]
            tvOutDateTime.text =
                if (out.size - 1 >= position) out[position].time.split(" ")[1] else {
                    "-"
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackingDetailBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return login.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(login[position], position)
    }
}
