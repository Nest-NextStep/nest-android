package com.bangkit.nest.ui.main.catalog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.databinding.ItemAnotherMajorBinding
import com.bangkit.nest.databinding.ItemRecommendedMajorBinding
import com.bangkit.nest.ui.main.home.HomeFragment
import com.bangkit.nest.utils.dpToPx

class ListMajorAdapter(
    private val type: String,
    private val fragment: CatalogFragment? = null,
    private val fragmentHome: HomeFragment? = null
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

        if (fragmentHome != null) {
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.width = 320.dpToPx(holder.itemView.context)
            layoutParams.bottomMargin = 6.dpToPx(holder.itemView.context)
            val lMargin = holder.itemView.context.resources.getDimension(R.dimen.l_margin).toInt()
            val xsMargin = holder.itemView.context.resources.getDimension(R.dimen.xs_margin).toInt()
            when (position) {
                0 -> {
                    // First item
                    layoutParams.marginStart = lMargin
                    layoutParams.marginEnd = xsMargin
                }
                itemCount - 1 -> {
                    // Last item
                    layoutParams.marginStart = xsMargin
                    layoutParams.marginEnd = lMargin
                }
                else -> {
                    // Middle items
                    layoutParams.marginStart = xsMargin
                    layoutParams.marginEnd = xsMargin
                }
            }
            holder.itemView.layoutParams = layoutParams
        }

        when (holder) {
            is RecommendedMajorViewHolder -> holder.bind(item)
            is AnotherMajorViewHolder -> holder.bind(item)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            item.majorId?.let {
                bundle.putInt("itemId", it)
            }

            if (fragment != null) {
                fragment.findNavController().navigate(R.id.action_catalog_to_catalogDetail, bundle)
            } else if (fragmentHome != null) {
                fragmentHome.findNavController().navigate(R.id.action_home_to_catalogDetail, bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        return itemCount
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
        }
    }

    inner class AnotherMajorViewHolder(val binding: ItemAnotherMajorBinding) :
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
                return oldItem.majorId == newItem.majorId
            }
        }
    }
}