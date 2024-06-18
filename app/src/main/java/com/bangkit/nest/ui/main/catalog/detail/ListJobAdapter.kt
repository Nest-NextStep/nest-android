package com.bangkit.nest.ui.main.catalog.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.MajorJobItem
import com.bangkit.nest.databinding.ItemJobBinding

class ListJobAdapter() : ListAdapter<MajorJobItem, ListJobAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val job = getItem(position)
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
        holder.bind(job)
    }

    class MyViewHolder(private val binding: ItemJobBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(job: MajorJobItem) {
            binding.textViewJobName.text = job.jobsName
            binding.textViewJobDescription.text = job.jobsDescription
            binding.textViewJobSalaryRange.text = job.jobsSalary
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MajorJobItem> =
            object : DiffUtil.ItemCallback<MajorJobItem>() {
                override fun areItemsTheSame(oldItem: MajorJobItem, newItem: MajorJobItem): Boolean {
                    return oldItem.jobsId == newItem.jobsId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: MajorJobItem, newItem: MajorJobItem): Boolean {
                    return oldItem.jobsName == newItem.jobsName
                }
            }
    }
}