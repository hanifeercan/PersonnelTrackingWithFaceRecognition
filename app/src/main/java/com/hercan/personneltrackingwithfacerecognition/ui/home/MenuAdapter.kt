package com.hercan.personneltrackingwithfacerecognition.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.databinding.ItemMenuBinding

class MenuAdapter(private val menuList: List<MenuItem>, private val authority: String) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var itemClickListener: ((MenuItem) -> Unit)? = null

    inner class ViewHolder(
        private val binding: ItemMenuBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem, position: Int) = with(binding) {

            text.text = root.context.getString(item.text)
            image.background = AppCompatResources.getDrawable(root.context, item.image)
            root.setOnClickListener {
                itemClickListener?.invoke(item)
            }
            if (position != 4 && position != 5) {
                if (authority == "yonetici") {
                    root.isClickable = true
                    itemMenu.foreground =
                        AppCompatResources.getDrawable(root.context, R.color.transparent)
                } else {
                    root.isClickable = false
                    itemMenu.foreground =
                        AppCompatResources.getDrawable(root.context, R.color.transparent_black)
                }
            }
        }
    }

    fun setItemClickListener(listener: (MenuItem) -> Unit) {
        this.itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuList[position], position)
    }
}
