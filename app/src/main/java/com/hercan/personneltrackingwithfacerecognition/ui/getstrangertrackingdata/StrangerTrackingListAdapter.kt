package com.hercan.personneltrackingwithfacerecognition.ui.getstrangertrackingdata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.databinding.ItemStrangerBinding
import com.squareup.picasso.Picasso

class StrangerTrackingListAdapter :
    ListAdapter<StrangerTrackingModel, StrangerTrackingListAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(
        private val binding: ItemStrangerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StrangerTrackingModel) = with(binding) {

            if (item.photoUrl != "") {
                image.setBackgroundResource(0)
                Picasso.get().load(item.photoUrl).error(R.drawable.ic_photo)
                    .placeholder(R.drawable.ic_photo).into(image)
                tvTime.text = item.time
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStrangerBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item.let {
            holder.bind(item)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<StrangerTrackingModel>() {
        override fun areItemsTheSame(
            oldItem: StrangerTrackingModel, newItem: StrangerTrackingModel
        ): Boolean {
            return oldItem.photoUrl == newItem.photoUrl
        }

        override fun areContentsTheSame(
            oldItem: StrangerTrackingModel, newItem: StrangerTrackingModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}