package com.bangkit.nest.ui.main.catalog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.databinding.ItemAnotherMajorBinding
import com.bangkit.nest.databinding.ItemRecommendedMajorBinding

class ListMajorAdapter(private val type: String) :
    ListAdapter<MajorItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_RECOMMENDED -> {
                val binding = ItemRecommendedMajorBinding.inflate(inflater, parent, false)
                RecommendedMajorViewHolder(binding)
            }
            TYPE_ANOTHER -> {
                val binding = ItemAnotherMajorBinding.inflate(inflater, parent, false)
                AnotherMajorViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is RecommendedMajorViewHolder -> holder.bind(item)
            is AnotherMajorViewHolder -> holder.bind(item)
        }

        holder.itemView.setOnClickListener {
            //TODO
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            "recommended" -> TYPE_RECOMMENDED
            "another" -> TYPE_ANOTHER
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    class RecommendedMajorViewHolder(val binding: ItemRecommendedMajorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MajorItem) {
            binding.textViewMajorName.text = item.majorName
            binding.textViewMajorDescription.text = item.majorDescription
        }
    }

    class AnotherMajorViewHolder(val binding: ItemAnotherMajorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MajorItem) {
            binding.textViewMajorName.text = item.majorName
            binding.textViewMajorDescription.text = item.majorDescription
        }
    }

    companion object {
        private const val TYPE_RECOMMENDED = 0
        private const val TYPE_ANOTHER = 1

        private val DIFF_CALLBACK= object : DiffUtil.ItemCallback<MajorItem>() {
            override fun areItemsTheSame(oldItem: MajorItem, newItem: MajorItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MajorItem, newItem: MajorItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}