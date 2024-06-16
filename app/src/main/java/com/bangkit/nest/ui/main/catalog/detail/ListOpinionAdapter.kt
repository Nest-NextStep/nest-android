package com.bangkit.nest.ui.main.catalog.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.MajorOpinionItem
import com.bangkit.nest.databinding.ItemOpinionBinding

class ListOpinionAdapter(private val fragment: CatalogDetailFragment) : ListAdapter<MajorOpinionItem, ListOpinionAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemOpinionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val opinion = getItem(position)
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
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
        holder.bind(opinion)
    }

    inner class MyViewHolder(private val binding: ItemOpinionBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(opinion: MajorOpinionItem) {
            binding.textViewStudentName.text = opinion.opinionName
            binding.textViewStudentOpinion.text = opinion.opinionsContent

//            val majorUni = "$opinion.major - $opinion.uni"
            val majorUni = "Sistem Informasi - Universitas Indonesia"
            binding.textViewMajorUni.text = majorUni

            itemView.setOnClickListener {
                fragment.showOpinionDialog(opinion.opinionName, majorUni, opinion.opinionsContent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MajorOpinionItem> =
            object : DiffUtil.ItemCallback<MajorOpinionItem>() {
                override fun areItemsTheSame(oldItem: MajorOpinionItem, newItem: MajorOpinionItem): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: MajorOpinionItem, newItem: MajorOpinionItem): Boolean {
                    return oldItem.opinionId == newItem.opinionId
                }
            }
    }
}