package com.android.infinum.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.infinum.R
import com.android.infinum.databinding.ShowsItemBinding
import com.android.infinum.models.ShowsModel
import com.android.infinum.models.responses.ShowResponse

class ShowsAdapter(
    private var items: List<ShowResponse>,
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
        holder.bind(items.get(position))
    }

    inner class ShowsViewHolder(private val binding: ShowsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShowResponse) {

            binding.apply {
                showName.text = item.title
                showItemImage.setImageResource(R.drawable.daredevil)
                showDescription.text = item.description
            }

            binding.root.setOnClickListener {
                onClickCallback(item.id)
            }
        }
    }
}
