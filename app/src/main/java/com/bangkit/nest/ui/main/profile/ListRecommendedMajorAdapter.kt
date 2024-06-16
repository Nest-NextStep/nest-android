package com.bangkit.nest.ui.main.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.databinding.ItemRecommendedMajorProfileBinding

class ListRecommendedMajorAdapter() : ListAdapter<MajorItem, ListRecommendedMajorAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecommendedMajorProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val job = getItem(position)
        when (position) {
            0 -> {
                val layoutParams = holder.itemView.layoutParams as MarginLayoutParams
                layoutParams.marginStart =
                    holder.itemView.context.resources.getDimension(R.dimen.l_margin)
                        .toInt()
                holder.itemView.layoutParams = layoutParams
                layoutParams.marginEnd =
                    holder.itemView.context.resources.getDimension(R.dimen.xs_margin)
                        .toInt()
                holder.itemView.layoutParams = layoutParams
            }
            itemCount - 1 -> {
                val layoutParams = holder.itemView.layoutParams as MarginLayoutParams
                layoutParams.marginEnd =
                    holder.itemView.context.resources.getDimension(R.dimen.l_margin)
                        .toInt()
                layoutParams.marginStart =
                    holder.itemView.context.resources.getDimension(R.dimen.xs_margin)
                        .toInt()
                holder.itemView.layoutParams = layoutParams
            }
            else -> {
                val layoutParams = holder.itemView.layoutParams as MarginLayoutParams
                layoutParams.marginStart =
                    holder.itemView.context.resources.getDimension(R.dimen.xs_margin)
                        .toInt()
                layoutParams.marginEnd =
                    holder.itemView.context.resources.getDimension(R.dimen.xs_margin)
                        .toInt()
                holder.itemView.layoutParams = layoutParams
            }
        }
        holder.bind(job)
    }

    class MyViewHolder(private val binding: ItemRecommendedMajorProfileBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(major: MajorItem) {
            binding.majorNameTextView.text = major.majorName
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MajorItem> =
            object : DiffUtil.ItemCallback<MajorItem>() {
                override fun areItemsTheSame(oldItem: MajorItem, newItem: MajorItem): Boolean {
                    return oldItem.majorId == newItem.majorId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: MajorItem, newItem: MajorItem): Boolean {
                    return oldItem.majorName == newItem.majorName
                }
            }
    }
}