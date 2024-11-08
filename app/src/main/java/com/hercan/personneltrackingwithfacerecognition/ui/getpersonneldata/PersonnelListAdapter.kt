package com.hercan.personneltrackingwithfacerecognition.ui.getpersonneldata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.databinding.ItemPersonnelListBinding
import com.squareup.picasso.Picasso

class PersonnelListAdapter(private val menuList: List<Personnel>) :
    RecyclerView.Adapter<PersonnelListAdapter.ViewHolder>() {

    private var itemClickListener: ((Personnel) -> Unit)? = null

    inner class ViewHolder(
        private val binding: ItemPersonnelListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Personnel) = with(binding) {

            text.text = item.name
            if (item.photo != "") {
                image.setBackgroundResource(0)
                Picasso.get().load(item.photo).error(R.drawable.ic_photo)
                    .placeholder(R.drawable.ic_photo).into(image)
            }
            root.setOnClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }

    fun setItemClickListener(listener: (Personnel) -> Unit) {
        this.itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPersonnelListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuList[position])
    }
}
