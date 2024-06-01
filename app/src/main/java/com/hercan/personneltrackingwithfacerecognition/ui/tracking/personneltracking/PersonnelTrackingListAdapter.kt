package com.hercan.personneltrackingwithfacerecognition.ui.tracking.personneltracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hercan.personneltrackingwithfacerecognition.databinding.ItemTrackingBinding

class PersonnelTrackingListAdapter(private val list: List<PersonnelTrackingItem>) :
    RecyclerView.Adapter<PersonnelTrackingListAdapter.ViewHolder>() {

    private var itemClickListener: ((PersonnelTrackingItem) -> Unit)? = null

    inner class ViewHolder(
        private val binding: ItemTrackingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PersonnelTrackingItem) = with(binding) {

            text.text = item.name
            root.setOnClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }

    fun setItemClickListener(listener: (PersonnelTrackingItem) -> Unit) {
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
