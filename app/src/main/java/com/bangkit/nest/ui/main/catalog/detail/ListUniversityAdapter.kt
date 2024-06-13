package com.bangkit.nest.ui.main.catalog.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.data.remote.response.MajorUniversityItem
import com.bangkit.nest.databinding.ItemUniversityBinding
import com.bumptech.glide.Glide

class ListUniversityAdapter() : ListAdapter<MajorUniversityItem, ListUniversityAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUniversityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val university = getItem(position)
        holder.bind(university)
    }

    class MyViewHolder(private val binding: ItemUniversityBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(university: MajorUniversityItem) {
            Glide.with(itemView.context)
                .load(university.universityImage)
                .into(binding.imageViewUniversityLogo)

            binding.textViewUniversityName.text = university.universityName
            binding.textUniversityLocation.text = university.universityLocation
            val accreditation = "Accreditation: ${university.universityAcreditation}"
            binding.textViewUniversityAccreditation.text = accreditation

            binding.buttonUniversity.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(university.universityLink))
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MajorUniversityItem> =
            object : DiffUtil.ItemCallback<MajorUniversityItem>() {
                override fun areItemsTheSame(oldItem: MajorUniversityItem, newItem: MajorUniversityItem): Boolean {
                    return oldItem.universityId == newItem.universityId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: MajorUniversityItem, newItem: MajorUniversityItem): Boolean {
                    return oldItem.universityName == newItem.universityName
                }
            }
    }
}