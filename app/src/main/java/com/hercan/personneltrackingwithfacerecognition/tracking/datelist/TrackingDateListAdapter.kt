package com.hercan.personneltrackingwithfacerecognition.tracking.datelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hercan.personneltrackingwithfacerecognition.databinding.ItemTrackingBinding

class TrackingDateListAdapter(private val list: List<String>) :
    RecyclerView.Adapter<TrackingDateListAdapter.ViewHolder>() {

    private var itemClickListener: ((String) -> Unit)? = null

    inner class ViewHolder(
        private val binding: ItemTrackingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dateStr: String) = with(binding) {

            text.text = dateStr
            root.setOnClickListener {
                itemClickListener?.invoke(dateStr)
            }
        }
    }

    fun setItemClickListener(listener: (String) -> Unit) {
        this.itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackingBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
