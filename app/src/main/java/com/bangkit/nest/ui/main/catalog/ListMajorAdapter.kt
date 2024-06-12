package com.bangkit.nest.ui.main.catalog

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.data.repository.MajorRepository
import com.bangkit.nest.databinding.ItemAnotherMajorBinding
import com.bangkit.nest.databinding.ItemRecommendedMajorBinding

class ListMajorAdapter(
    private val type: String,
    private val fragment: CatalogFragment
) :
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
    }

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            "recommended" -> TYPE_RECOMMENDED
            "another" -> TYPE_ANOTHER
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    inner class RecommendedMajorViewHolder(val binding: ItemRecommendedMajorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MajorItem) {
            binding.textViewMajorName.text = item.majorName
            binding.textViewMajorDescription.text = item.majorDescription

            itemView.setOnClickListener {
                Log.d("ListMajorAdapter", "clicked")
                item.majorId?.let { fragment.saveMajorId(it) }
                fragment.findNavController().navigate(R.id.action_catalog_to_catalogDetail)
            }
        }
    }

    inner class AnotherMajorViewHolder(val binding: ItemAnotherMajorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MajorItem) {
            binding.textViewMajorName.text = item.majorName
            binding.textViewMajorDescription.text = item.majorDescription

            itemView.setOnClickListener {
                Log.d("ListMajorAdapter", "clicked")
                item.majorId?.let { fragment.saveMajorId(it) }
                fragment.findNavController().navigate(R.id.action_catalog_to_catalogDetail)
            }
        }
    }

    companion object {
        private const val TYPE_RECOMMENDED = 0
        private const val TYPE_ANOTHER = 1

        private val DIFF_CALLBACK= object : DiffUtil.ItemCallback<MajorItem>() {
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