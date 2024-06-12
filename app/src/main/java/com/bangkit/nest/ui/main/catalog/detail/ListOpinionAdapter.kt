package com.bangkit.nest.ui.main.catalog.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.R
import com.bangkit.nest.data.remote.response.MajorOpinionItem
import com.bangkit.nest.databinding.ItemOpinionBinding
import com.bumptech.glide.Glide


class ListOpinionAdapter() : ListAdapter<MajorOpinionItem, ListOpinionAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemOpinionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val opinion = getItem(position)
        holder.bind(opinion)

        if (position == 0) {
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginStart =
                holder.itemView.context.resources.getDimension(R.dimen.xl_corner_radius)
                    .toInt()
            holder.itemView.layoutParams = layoutParams
        }

        if (position == itemCount - 1) {
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginEnd =
                holder.itemView.context.resources.getDimension(R.dimen.xl_corner_radius)
                    .toInt()
            holder.itemView.layoutParams = layoutParams
        }
    }

    class MyViewHolder(private val binding: ItemOpinionBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(opinion: MajorOpinionItem) {
            binding.textViewStudentName.text = opinion.opinionName
            binding.textViewStudentOpinion.text = opinion.opinionsContent
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MajorOpinionItem> =
            object : DiffUtil.ItemCallback<MajorOpinionItem>() {
                override fun areItemsTheSame(oldItem: MajorOpinionItem, newItem: MajorOpinionItem): Boolean {
                    return oldItem.opinionId == newItem.opinionId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: MajorOpinionItem, newItem: MajorOpinionItem): Boolean {
                    return oldItem.opinionId == newItem.opinionId
                }
            }
    }
}