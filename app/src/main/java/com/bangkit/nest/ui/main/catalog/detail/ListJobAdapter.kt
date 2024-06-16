package com.bangkit.nest.ui.main.catalog.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
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
        holder.bind(job)

        if (position == 0) {
            val layoutParams = holder.itemView.layoutParams as MarginLayoutParams
            layoutParams.marginStart =
                holder.itemView.context.resources.getDimension(R.dimen.l_margin)
                    .toInt()
            holder.itemView.layoutParams = layoutParams
        }

        if (position == itemCount - 1) {
            val layoutParams = holder.itemView.layoutParams as MarginLayoutParams
            layoutParams.marginEnd =
                holder.itemView.context.resources.getDimension(R.dimen.l_margin)
                    .toInt()
            holder.itemView.layoutParams = layoutParams
        }
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