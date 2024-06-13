package com.bangkit.nest.ui.main.assess.question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nest.databinding.ItemOptionBinding
import com.bangkit.nest.data.remote.response.OptionDataItem

class OptionAdapter(
    private val options: List<OptionDataItem>,
    private val onOptionSelected: (OptionDataItem) -> Unit
) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private var selectedOption: OptionDataItem? = null

    inner class OptionViewHolder(private val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: OptionDataItem) {
            binding.optionButton.text = option.optionText
            binding.optionButton.isSelected = option == selectedOption
            binding.optionButton.setOnClickListener {
                selectedOption = option
                onOptionSelected(option)
                notifyDataSetChanged()
            }
            binding.optionButton.isSelected = option == selectedOption
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount() = options.size
}
