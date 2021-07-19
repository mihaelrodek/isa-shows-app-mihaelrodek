package com.android.infinum

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.infinum.databinding.ShowsItemBinding

class ShowsAdapter(
    private var items: List<ShowsModel>,
    private val onClickCallback: (String) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {

        val binding = ShowsItemBinding.inflate(LayoutInflater.from(parent.context))

        return ShowsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(superheros: List<ShowsModel>) {
        items = superheros
        notifyDataSetChanged()
    }

    inner class ShowsViewHolder(private val binding: ShowsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShowsModel) {

            binding.showName.text = item.name
            binding.showItemImage.setImageResource(item.imageResourceId)
            binding.showDescription.text = item.description

            binding.root.setOnClickListener {
                onClickCallback(item.id)
            }

        }
    }
}
